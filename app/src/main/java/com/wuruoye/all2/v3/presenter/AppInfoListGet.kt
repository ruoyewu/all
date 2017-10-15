package com.wuruoye.all2.v3.presenter

import android.content.Context
import com.google.gson.Gson
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.v3.model.AppInfo
import com.wuruoye.all2.v3.model.AppInfoCache
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class AppInfoListGet(context: Context) : AbsPresenter<AbsView<ArrayList<String>>>(), Listener<ArrayList<String>> {
    private val appInfoCache = AppInfoCache(context)

    override fun onSuccess(model: ArrayList<String>) {
        getView()?.setModel(model)
    }

    override fun onFail(message: String) {
        getView()?.setWorn(message)
    }

    fun requestAppInfoList(method: Method) {
        var m = method
        if (m == Method.LOCAL){
            val info = appInfoCache.getAlAppList()
            if (info.size == 0){
                m = Method.NET
            }else{
                onSuccess(appInfoCache.getAvAppList())
            }
        }
        if (m == Method.NET){
            NetUtil.get(Config.APP_LIST_URL, object : Listener<String>{
                override fun onSuccess(model: String) {
                    val list = parseData(model)
                    this@AppInfoListGet.onSuccess(list)
                }

                override fun onFail(message: String) {
                    this@AppInfoListGet.onFail(message)
                }
            })
        }
    }

    private fun parseData(data: String): ArrayList<String>{
        val json = JSONObject(data)

        // 获取 nameList
        val avInfoList = json.getJSONArray("list");
        val string_list = ArrayList<String>()
        if (appInfoCache.getAvAppList().size == 0){
            (0 until avInfoList.length())
                    .map { avInfoList.getString(it) }
                    .mapTo(string_list) {it}
            appInfoCache.putAvAppList(string_list)
        }

        // 保存所有 app 详情
        val alInfoList = json.getString("info_list");
        appInfoCache.putAlAppList(alInfoList)

        // 保存 所有 name 对应的 icon
        val alArray = JSONArray(alInfoList)
        for (i in 0 until alArray.length()){
            val obj = alArray.getJSONObject(i)
            val name = obj.getString("name")
            val icon = obj.getString("icon")
            appInfoCache.putAppIcon(name, icon)
        }

        return appInfoCache.getAvAppList()
    }
}