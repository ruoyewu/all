package com.wuruoye.all2.setting

import android.os.Bundle
import android.support.v4.app.Fragment
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.widget.SlideRelativeLayout
import com.wuruoye.all2.v3.adapter.FragmentVPAdapter
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * Created by wuruoye on 2017/10/15.
 * this file is to do
 */
class SettingActivity : BaseActivity() {
    override val contentView: Int
        get() = R.layout.activity_setting

    override fun initData(bundle: Bundle?) {

    }

    override fun initView() {
        overridePendingTransition(R.anim.activity_open_right, R.anim.activity_no)

        activity_setting.childType = SlideRelativeLayout.ChildType.VIEWPAGER
        activity_setting.slideType = SlideRelativeLayout.SlideType.HORIZONTAL
        activity_setting.attachViewPager(vp_setting)
        activity_setting.setOnSlideListener(object : SlideRelativeLayout.OnSlideListener{
            override fun onClosePage() {
                finish()
                overridePendingTransition(R.anim.activity_no, R.anim.activity_no)
            }
            override fun translatePage(progress: Float) {

            }

        })

        val fragmentList = arrayListOf<Fragment>(AutoSettingFragment(), AppManagerFragment())
        val titleList = arrayListOf("常规", "首页展示")
        val adapter = FragmentVPAdapter(supportFragmentManager, fragmentList, titleList)
        vp_setting.adapter = adapter
        tl_setting.setupWithViewPager(vp_setting)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.activity_no, R.anim.activity_close_right)
    }
}