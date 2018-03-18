package com.wuruoye.all2.setting

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseFragment
import com.wuruoye.all2.setting.adapter.AppManagerRVAdapter2
import com.wuruoye.all2.setting.model.bean.ManageredApp
import com.wuruoye.all2.v3.model.AppInfoCache
import kotlinx.android.synthetic.main.fragment_app_manager.*
import java.util.ArrayList

/**
 * Created by wuruoye on 2017/10/16.
 * this file is to do
 */
class AppManagerFragment : BaseFragment(), AppManagerRVAdapter2.OnActionListener {
    private lateinit var mAppInfoCache: AppInfoCache

    override val contentView: Int
        get() = R.layout.fragment_app_manager

    override fun initData(bundle: Bundle?) {
        mAppInfoCache = AppInfoCache(context)
    }

    override fun initView(view: View) {
        val adapter = AppManagerRVAdapter2(mAppInfoCache.getManageredApp())
        adapter.setOnActionListener(this)
        rv_app_manager.layoutManager = LinearLayoutManager(context)
        rv_app_manager.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(AppManagerRVAdapter2.TouchHelperCallBack(adapter))
        itemTouchHelper.attachToRecyclerView(rv_app_manager)
    }

    override fun onCheckChange(appList: ArrayList<ManageredApp>) {
        val adapter = rv_app_manager.adapter as AppManagerRVAdapter2
        mAppInfoCache.putManageredApp(adapter.getResult())
    }

    override fun onPositionChange(appList: ArrayList<ManageredApp>) {
        val adapter = rv_app_manager.adapter as AppManagerRVAdapter2
        mAppInfoCache.putManageredApp(adapter.getResult())
    }
}