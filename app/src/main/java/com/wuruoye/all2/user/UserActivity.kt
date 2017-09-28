package com.wuruoye.all2.user

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.View
import com.transitionseverywhere.Fade
import com.transitionseverywhere.TransitionManager
import com.wuruoye.all2.R

import com.wuruoye.all2.base.PhotoActivity
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.util.*
import com.wuruoye.all2.user.model.AppBarStateChangeListener
import com.wuruoye.all2.user.model.UserCache
import com.wuruoye.all2.v3.adapter.FragmentVPAdapter
import kotlinx.android.synthetic.main.activity_user.*

/**
 * Created by wuruoye on 2017/9/23.
 * this file is to do
 */

class UserActivity : PhotoActivity() {

    private lateinit var mUserCache: UserCache
    private lateinit var mUserName: String
    private var isRefresh = false
    private var currentItem = 0

    private val mFragmentList = ArrayList<Fragment>()

    override val contentView: Int
        get() = R.layout.activity_user

    override fun initData(bundle: Bundle?) {
        mUserName = bundle!!.getString("username")

        mUserCache = UserCache(applicationContext)
    }

    override fun initView() {
        initUser()
        setUserVP()

        al_user.addOnOffsetChangedListener(object : AppBarStateChangeListener(){
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {
                onStateChange(state)
            }
        })
        fab_user.setOnClickListener {
            startRefresh()
        }

        ll_user_center.setOnClickListener {
            onAvatarClick()
        }
        ll_user_top.setOnClickListener {
            if (ll_user_top.visibility == View.VISIBLE){
                onAvatarClick()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOGIN){
            mUserName = mUserCache.userName
            initUser()
        }
    }

    override fun onTakePhoto(filePath: String) {
        setAvatar(filePath)
    }

    override fun onChoosePhoto(filePath: String) {
        setAvatar(filePath)
    }

    private fun setAvatar(filePath: String){
        val bitmap = BitmapFactory.decodeFile(filePath)

        civ_user_avatar1.setImageBitmap(bitmap)
        civ_user_avatar2.setImageBitmap(bitmap)
        iv_user_head.setImageBitmap(
                BlurUtil.blurBitmap(applicationContext, bitmap)
        )
        NetUtil.postFile(Config.USER_AVATAR_URL, filePath, mUserCache.userName, object : Listener<String>{
            override fun onSuccess(model: String) {
                mUserCache.userAvatar = filePath
            }

            override fun onFail(message: String) {
                loge("fail : " + message)
            }

        })
    }

    private fun onAvatarClick(){
        if (mUserCache.userName != ""){
            if (mUserName == mUserCache.userName && mUserCache.isLogin){
                AlertDialog.Builder(this)
                        .setItems(dialog_items, { _, which ->
                            when (which){
                                0 -> {      // 注销登录
                                    mUserCache.cancelUser()
                                    mUserName = ""
                                    initUser()
                                }
                                1 -> {      //相册
                                    choosePhoto()
                                }
                                2 -> {      //相机
                                    takePhoto("avatar.jpg")
                                }
                            }
                        })
                        .show()
            }else{

            }
        }else{
            startActivityForResult(Intent(this, LoginActivity::class.java), LOGIN)
        }
    }

    private fun initUser(){
        if (mUserName != ""){
            if (mUserName == mUserCache.userName && mUserCache.isLogin){
                //自己的详情页面
                fab_user.visibility = View.VISIBLE
                tv_user_name1.text = mUserName
                tv_user_name2.text = mUserName

                try {
                    val avatarPath = mUserCache.userAvatar
                    if (avatarPath != ""){
                        val avatarBitmap = BitmapFactory.decodeFile(avatarPath)
                        civ_user_avatar1.setImageBitmap(avatarBitmap)
                        civ_user_avatar2.setImageBitmap(avatarBitmap)

                        iv_user_head.setImageBitmap(
                                BlurUtil.blurBitmap(applicationContext, avatarBitmap)
                        )
                    }
                } catch (e: Exception) {
                    civ_user_avatar1.setImageResource(R.drawable.ic_avatar)
                    civ_user_avatar2.setImageResource(R.drawable.ic_avatar)
                }
            }else{
                //别人的详情页面
                fab_user.visibility = View.GONE

                tv_user_name1.text = mUserName
                tv_user_name2.text = mUserName
                loadUserImage(mUserName, civ_user_avatar1)
                loadUserImage(mUserName, civ_user_avatar2)

                getImageBitmap(Config.USER_AVATAR_URL + "/" + mUserName, object : Listener<Bitmap>{
                    override fun onSuccess(model: Bitmap) {
                        runOnUiThread {
                            iv_user_head.setImageBitmap(BlurUtil.blurBitmap(applicationContext, model))
                        }
                    }

                    override fun onFail(message: String) {

                    }

                })
            }
        }else{
            civ_user_avatar1.setImageResource(R.drawable.ic_avatar)
            civ_user_avatar2.setImageResource(R.drawable.ic_avatar)
            tv_user_name1.text = "点击登录"
            tv_user_name2.text = "点击登录"
            iv_user_head.setImageResource(0)

        }
    }

    private fun setUserVP(){
        mFragmentList.clear()
        val bundle = Bundle()
        bundle.putString("username", mUserName)

        val userFavoriteFragment = UserFavoriteFragment()
        userFavoriteFragment.arguments = bundle
        mFragmentList.add(userFavoriteFragment)
        mFragmentList.add(UserCourseFragment())
        mFragmentList.add(UserInfoFragment())

        val adapter = FragmentVPAdapter(supportFragmentManager, mFragmentList, title_items)
        vp_user.adapter = adapter
        tl_user.setupWithViewPager(vp_user)

        vp_user.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                currentItem = position
            }

        })
    }

    private fun startRefresh(){
        if (!isRefresh){
            isRefresh = true
            fab_user.start()
            val fragment = mFragmentList[currentItem] as RefreshFragment
            fragment.refresh()
        }
    }

    fun stopRefresh(){
        isRefresh = false
        fab_user.stop()
    }

    private fun onStateChange(state: AppBarStateChangeListener.State){
        TransitionManager.beginDelayedTransition(al_user, Fade())
        if (state == AppBarStateChangeListener.State.COLLAPSED){
            ll_user_top.visibility = View.VISIBLE
            ll_user_center.visibility = View.INVISIBLE
        }else{
            ll_user_top.visibility = View.INVISIBLE
            ll_user_center.visibility = View.VISIBLE
        }
    }

    companion object {
        val title_items = arrayListOf(
                "收藏文章", "个人历程", "个人信息"
        )
        val dialog_items = arrayOf(
                "注销登录", "打开相册", "打开相机"
        )
        val LOGIN = 3
    }
}
