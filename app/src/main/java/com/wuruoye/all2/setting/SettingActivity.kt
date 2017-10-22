package com.wuruoye.all2.setting

import android.os.Bundle
import android.support.v4.app.Fragment
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.BaseSlideActivity
import com.wuruoye.all2.base.widget.SlideLayout
import com.wuruoye.all2.v3.adapter.FragmentVPAdapter
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * Created by wuruoye on 2017/10/15.
 * this file is to do
 */
class SettingActivity : BaseSlideActivity() {
    override val contentView: Int
        get() = R.layout.activity_setting

    override val childType: SlideLayout.ChildType
        get() = SlideLayout.ChildType.VIEWPAGER

    override val slideType: SlideLayout.SlideType
        get() = SlideLayout.SlideType.HORIZONTAL

    override val initAfterOpen: Boolean
        get() = false

    override fun initData(bundle: Bundle?) {

    }

    override fun initView() {
        getSlideLayout()?.attachViewPager(vp_setting)

        val fragmentList = arrayListOf<Fragment>(AutoSettingFragment(), AppManagerFragment())
        val titleList = arrayListOf("常规", "首页展示")
        val adapter = FragmentVPAdapter(supportFragmentManager, fragmentList, titleList)
        vp_setting.adapter = adapter
        tl_setting.setupWithViewPager(vp_setting)
    }
}