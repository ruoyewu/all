package com.wuruoye.all2.v3

import android.os.Bundle
import android.support.v4.app.Fragment
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.v3.adapter.AppVPAdapter
import com.wuruoye.all2.v3.model.AppInfo
import kotlinx.android.synthetic.main.activity_app_info.*

/**
 * Created by wuruoye on 2017/9/17.
 * this file is to do
 */
class AppInfoActivity : BaseActivity() {

    private lateinit var mAppInfo: AppInfo
    private val mFragments = ArrayList<Fragment>()

    override val contentView: Int
        get() = R.layout.activity_app_info

    override fun initData(bundle: Bundle?) {
        mAppInfo = bundle!!.getParcelable("info")
    }

    override fun initView() {
        for (i in mAppInfo.category_name){
            val bundle = Bundle()
            bundle.putString("name", mAppInfo.name)
            bundle.putString("category", i)
            val fragment = AppListFragment()
            fragment.arguments = bundle
            mFragments.add(fragment)
        }
        val adapter = AppVPAdapter(supportFragmentManager, mFragments, mAppInfo.category_title)
        vp_app.adapter = adapter
        tl_app.setupWithViewPager(vp_app)
    }
}