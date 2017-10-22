package com.wuruoye.all2.v3

import android.os.Bundle
import android.support.v4.app.Fragment
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseSlideActivity
import com.wuruoye.all2.base.util.loge
import com.wuruoye.all2.base.widget.SlideLayout
import com.wuruoye.all2.v3.adapter.FragmentVPAdapter
import com.wuruoye.all2.v3.model.bean.AppInfo
import kotlinx.android.synthetic.main.activity_app_info.*

/**
 * Created by wuruoye on 2017/9/17.
 * this file is to do
 */
class AppInfoActivity : BaseSlideActivity() {
    private lateinit var mAppInfo: AppInfo
    private val mFragments = ArrayList<Fragment>()

    override val contentView: Int
        get() = R.layout.activity_app_info

    override val childType: SlideLayout.ChildType
        get() = SlideLayout.ChildType.VIEWPAGER

    override val slideType: SlideLayout.SlideType
        get() = SlideLayout.SlideType.HORIZONTAL

    override val initAfterOpen: Boolean
        get() = false

    override fun initData(bundle: Bundle?) {
        mAppInfo = bundle!!.getParcelable("info")
    }

    override fun initView() {

        getSlideLayout()?.attachViewPager(vp_app)

        for (i in mAppInfo.category_name) {
            val bundle = Bundle()
            bundle.putString("name", mAppInfo.name)
            bundle.putString("category", i)
            val fragment = AppListFragment()
            fragment.arguments = bundle
            mFragments.add(fragment)
        }
        loge("start")
        val adapter = FragmentVPAdapter(supportFragmentManager, mFragments, mAppInfo.category_title)
        vp_app.adapter = adapter
        tl_app.setupWithViewPager(vp_app)
    }
}