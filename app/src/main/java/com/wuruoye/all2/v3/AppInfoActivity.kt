package com.wuruoye.all2.v3

import android.os.Bundle
import android.support.v4.app.Fragment
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.widget.SlideRelativeLayout
import com.wuruoye.all2.v3.adapter.FragmentVPAdapter
import com.wuruoye.all2.v3.model.bean.AppInfo
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
        overridePendingTransition(R.anim.activity_open_right, R.anim.activity_no)
        for (i in mAppInfo.category_name){
            val bundle = Bundle()
            bundle.putString("name", mAppInfo.name)
            bundle.putString("category", i)
            val fragment = AppListFragment()
            fragment.arguments = bundle
            mFragments.add(fragment)
        }
        val adapter = FragmentVPAdapter(supportFragmentManager, mFragments, mAppInfo.category_title)
        vp_app.adapter = adapter
        tl_app.setupWithViewPager(vp_app)

        activity_list.childType = SlideRelativeLayout.ChildType.VIEWPAGER
        activity_list.slideType = SlideRelativeLayout.SlideType.HORIZONTAL
        activity_list.attachViewPager(vp_app)
        activity_list.setOnSlideListener(object : SlideRelativeLayout.OnSlideListener{
            override fun onClosePage() {
                finish()
                overridePendingTransition(R.anim.activity_no, R.anim.activity_no)
            }
            override fun translatePage(progress: Float) {

            }
        })
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.activity_no, R.anim.activity_close_right)
    }
}