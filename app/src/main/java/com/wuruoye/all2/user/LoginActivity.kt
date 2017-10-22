package com.wuruoye.all2.user

import android.os.Bundle
import android.support.v4.app.Fragment
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.BaseSlideActivity
import com.wuruoye.all2.base.widget.SlideLayout
import com.wuruoye.all2.v3.adapter.FragmentVPAdapter
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
class LoginActivity : BaseSlideActivity() {

    override val contentView: Int
        get() = R.layout.activity_login

    override val childType: SlideLayout.ChildType
        get() = SlideLayout.ChildType.VIEWPAGER

    override val slideType: SlideLayout.SlideType
        get() = SlideLayout.SlideType.HORIZONTAL

    override val initAfterOpen: Boolean
        get() = false

    override fun initData(bundle: Bundle?) {

    }

    override fun initView() {
        getSlideLayout()?.attachViewPager(vp_login)

        val fragmentList = arrayListOf<Fragment>(LoginLoginFragment(), LoginSignFragment())
        val titleList = arrayListOf<String>("登录", "注册")
        val adapter = FragmentVPAdapter(supportFragmentManager, fragmentList, titleList)
        vp_login.adapter = adapter
        tl_login.setupWithViewPager(vp_login)
    }

    fun onSignOk(){
        onBackPressed()
    }
}