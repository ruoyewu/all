package com.wuruoye.all2.user

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.transitionseverywhere.Fade
import com.transitionseverywhere.TransitionManager
import com.wuruoye.all2.R

import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.RefreshFragment
import com.wuruoye.all2.base.util.BlurUtil
import com.wuruoye.all2.base.util.extensions.loge
import com.wuruoye.all2.user.model.AppBarStateChangeListener
import com.wuruoye.all2.user.model.UserCache
import com.wuruoye.all2.v3.adapter.FragmentVPAdapter
import kotlinx.android.synthetic.main.activity_user.*

/**
 * Created by wuruoye on 2017/9/23.
 * this file is to do
 */

class UserActivity : BaseActivity() {
    private lateinit var userCache: UserCache
    private var isRefresh = false
    private var currentItem = 0

    private val mFragmentList = ArrayList<Fragment>()

    override val contentView: Int
        get() = R.layout.activity_user

    override fun initData(bundle: Bundle?) {
        userCache = UserCache(applicationContext)
    }

    override fun initView() {
//        nsv_user.isFillViewport = true
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
    }

    private fun initUser(){
        if (userCache.isLogin){
            tv_user_name1.text = userCache.userName
            tv_user_name2.text = userCache.userName
            tv_user_desc.text = userCache.userDesc
        }
        iv_user_head.setImageBitmap(
                BlurUtil.blurBitmap(applicationContext, BitmapFactory.decodeResource(resources, R.drawable.ic_avatar), 5f)
        )
    }

    private fun setUserVP(){
        mFragmentList.clear()
        mFragmentList.add(UserCollectFragment())
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

    fun changeUserInfo(){
        initUser()
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
    }
}
