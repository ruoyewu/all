package com.wuruoye.all2.user

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.transitionseverywhere.TransitionManager
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.base.util.toast
import com.wuruoye.all2.base.widget.SlideRelativeLayout
import com.wuruoye.all2.user.model.UserCache
import com.wuruoye.all2.user.presenter.LoginGet
import com.wuruoye.all2.user.presenter.LoginView
import com.wuruoye.all2.v3.adapter.FragmentVPAdapter
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
class LoginActivity : BaseActivity() {

    override val contentView: Int
        get() = R.layout.activity_login

    override fun initData(bundle: Bundle?) {

    }

    override fun initView() {
        overridePendingTransition(R.anim.activity_open_right, R.anim.activity_no)

        activity_login.childType = SlideRelativeLayout.ChildType.VIEWPAGER
        activity_login.slideType = SlideRelativeLayout.SlideType.HORIZONTAL
        activity_login.attachViewPager(vp_login)
        activity_login.setOnSlideListener(object : SlideRelativeLayout.OnSlideListener{
            override fun onClosePage() {
                finish()
                overridePendingTransition(R.anim.activity_no, R.anim.activity_no)
            }

            override fun translatePage(progress: Float) {

            }

        })

        val fragmentList = arrayListOf<Fragment>(LoginLoginFragment(), LoginSignFragment())
        val titleList = arrayListOf<String>("登录", "注册")
        val adapter = FragmentVPAdapter(supportFragmentManager, fragmentList, titleList)
        vp_login.adapter = adapter
        tl_login.setupWithViewPager(vp_login)
    }

    fun onSignOk(){
        onBackPressed()
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.activity_no, R.anim.activity_close_right)
    }
}