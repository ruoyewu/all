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
import com.wuruoye.all2.base.widget.SlideLayout
import com.wuruoye.all2.user.model.AppBarStateChangeListener
import com.wuruoye.all2.user.model.UserCache
import com.wuruoye.all2.user.model.bean.ArticleFavorite
import com.wuruoye.all2.user.presenter.UserGet
import com.wuruoye.all2.user.presenter.UserView
import com.wuruoye.all2.v3.adapter.FragmentVPAdapter
import kotlinx.android.synthetic.main.activity_user.*

/**
 * Created by wuruoye on 2017/9/23.
 * this file is to do
 */

class UserActivity : PhotoActivity() {
    private lateinit var mUserGet: UserGet
    private lateinit var mUserCache: UserCache
    private var mUserId = 0
    private lateinit var mUserName: String
    private var isRefresh = false
    private var currentItem = 0
    private var isCurrentUser = false

    private val mUserView = object : UserView{
        override fun onFavoriteGet(model: ArticleFavorite) {

        }

        override fun onAvatarUpload(result: Boolean) {
            runOnUiThread {
                if (result){
                    mUserCache.isAvatarUpload = true
                    toast("头像上传成功")
                }else{
                    toast("头像上传失败")
                }
            }
        }

        override fun setWorn(message: String) {
            runOnUiThread {
                toast(message)
            }
        }

        override fun onReadTimeUpload(result: Boolean) {
            runOnUiThread {
                logOut()
            }
        }

    }

    private val mFragmentList = ArrayList<Fragment>()

    override val contentView: Int
        get() = R.layout.activity_user

    override fun initData(bundle: Bundle?) {
        mUserId = bundle!!.getInt("userid")
        mUserName = bundle.getString("username")
        mUserCache = UserCache(applicationContext)

        mUserGet = UserGet(this)
        mUserGet.attachView(mUserView)

        isCurrentUser = mUserCache.userName == mUserName

        mChildType = SlideLayout.ChildType.VIEWPAGER
        mSlideType = SlideLayout.SlideType.HORIZONTAL
        isInitAfterOpen = false
    }

    override fun initView() {
        getSlideLayout()?.attachViewPager(vp_user)
        PermissionUtil(this).requestPermission(Config.FILE_PERMISSION)

        initUser()
        setUserVP(mUserId)

        al_user.addOnOffsetChangedListener(object : AppBarStateChangeListener(){
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {
                onStateChange(state)
            }
        })
        fab_user.setOnClickListener {
            loge("refresh")
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

        vp_user.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                currentItem = position
                if (currentItem == 0){
                    fab_user.hide()
                }else if (mUserCache.isLogin && mUserName == mUserCache.userName){
                    fab_user.show()
                }
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loge("result: " + requestCode)
        if (requestCode == LOGIN){
            mUserName = mUserCache.userName
            mUserId = mUserCache.userId
            initUser()
            setUserVP(mUserId)
        }
    }

    override fun onPhotoBack(photoPath: String) {
        val bitmap = BitmapFactory.decodeFile(photoPath)

        civ_user_avatar1.setImageBitmap(bitmap)
        civ_user_avatar2.setImageBitmap(bitmap)
        iv_user_head.setImageBitmap(
                BlurUtil.blurBitmap(applicationContext, bitmap)
        )

        mUserCache.isAvatarUpload = false
        mUserGet.uploadAvatar(mUserId, photoPath)
        mUserCache.userAvatar = photoPath
    }

    private fun onAvatarClick(){
        if (mUserName != ""){
            if (mUserName == mUserCache.userName && mUserCache.isLogin){
                AlertDialog.Builder(this)
                        .setItems(dialog_items, { _, which ->
                            when (which){
                                0 -> {      // 注销登录
                                    mUserGet.uploadReadTime(mUserId, mUserCache.readTime)
                                }
                                1 -> {      //相册
                                    choosePhoto("avatar.jpg", 1, 1, 500, 500)
                                }
                                2 -> {      //相机
                                    takePhoto("avatar.jpg", 1, 1, 500, 500)
                                }
                            }
                        })
                        .show()
            }else{

            }
        }else{
            startToLogin()
        }
    }

    private fun initUser(){
        if (mUserId != 0 && mUserName != ""){
            if (mUserName == mUserCache.userName && mUserCache.isLogin){
                //自己的详情页面
                setUserInfo(mUserName, mUserCache.userAvatar)
            }else{
                //别人的详情页面
                tv_user_name1.text = mUserName
                tv_user_name2.text = mUserName
                loadUserImage(mUserId.toString(), civ_user_avatar1)
                loadUserImage(mUserId.toString(), civ_user_avatar2)

                getImageBitmap(Config.USER_AVATAR_URL + "/" + mUserId, object : Listener<Bitmap>{
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

    private fun logOut(){
        mUserCache.cancelUser()
        mUserName = ""
        mUserId = 0
        initUser()
        setUserVP(mUserId)
    }

    private fun setUserInfo(name: String, avatarPath: String){
        tv_user_name1.text = name
        tv_user_name2.text = name

        if (avatarPath != ""){
            try {
                val avatarBitmap = BitmapFactory.decodeFile(avatarPath)
                civ_user_avatar1.setImageBitmap(avatarBitmap)
                civ_user_avatar2.setImageBitmap(avatarBitmap)

                iv_user_head.setImageBitmap(
                        BlurUtil.blurBitmap(applicationContext, avatarBitmap)
                )
                if (!mUserCache.isAvatarUpload) {
                    mUserGet.uploadAvatar(mUserId, avatarPath)
                }
            } catch (e: Exception) {
                toast("加载头像失败，请重新选择头像...")
            }
        }
    }

    private fun setUserVP(userId: Int){
        mFragmentList.clear()
        if (userId != 0) {
            val bundle = Bundle()
            bundle.putInt("userid", userId)

            val userFavoriteFragment = UserFavoriteFragment()
            userFavoriteFragment.arguments = bundle
            mFragmentList.add(userFavoriteFragment)
            mFragmentList.add(UserCourseFragment())
            mFragmentList.add(UserInfoFragment())

        }else{
            mFragmentList.add(EmptyFragment())
            mFragmentList.add(EmptyFragment())
            mFragmentList.add(EmptyFragment())
        }
        val adapter = FragmentVPAdapter(supportFragmentManager, mFragmentList, title_items)
        vp_user.adapter = adapter
        tl_user.setupWithViewPager(vp_user)
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

    fun startToLogin(){
        startActivityForResult(Intent(this, LoginActivity::class.java), LOGIN)
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

    override fun onDestroy() {
        super.onDestroy()
        mUserGet.detachView()
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
