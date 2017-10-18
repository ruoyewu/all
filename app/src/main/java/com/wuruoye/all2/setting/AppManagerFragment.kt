package com.wuruoye.all2.setting

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseFragment
import com.wuruoye.all2.base.util.loge
import com.wuruoye.all2.setting.adapter.AppManagerRVAdapter
import com.wuruoye.all2.setting.adapter.TouchHelperCallBack
import com.wuruoye.all2.v3.model.AppInfoCache
import kotlinx.android.synthetic.main.fragment_app_manager.*

/**
 * Created by wuruoye on 2017/10/16.
 * this file is to do
 */
class AppManagerFragment : BaseFragment() {
    private lateinit var mAppInfoCache: AppInfoCache

    override val contentView: Int
        get() = R.layout.fragment_app_manager

    override fun initData(bundle: Bundle?) {
        mAppInfoCache = AppInfoCache(context)
    }

    override fun initView(view: View) {
        val appList = ArrayList<Array<String>>()
        val alMap = mAppInfoCache.getAlAppMap()
        val alList = alMap.toList()
        val avs = mAppInfoCache.getAvAppList()

        appList.add(arrayOf("av", "首页展示列表", ""))
        (0 until avs.size).
                mapTo(appList) { arrayOf(alMap[avs[it]]!!.name, alMap[avs[it]]!!.title, alMap[avs[it]]!!.icon) }
        appList.add(arrayOf("av", "首页不展示列表", ""))
        alList.filterNot { it.first in avs }
                .mapTo(appList) { arrayOf(it.second.name, it.second.title, it.second.icon) }

        val adapter = AppManagerRVAdapter(appList)
        rv_app_manager.layoutManager = LinearLayoutManager(context)
        rv_app_manager.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(TouchHelperCallBack(adapter))
        itemTouchHelper.attachToRecyclerView(rv_app_manager)
    }
}