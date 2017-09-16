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

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class AppInfoListGet(context: Context) : AbsPresenter<AbsView<ArrayList<AppInfo>>>(context), Listener<ArrayList<AppInfo>> {
    private val appInfoCache = AppInfoCache(context)

    override fun onSuccess(model: ArrayList<AppInfo>) {
        getView()!!.setModel(model)
    }

    override fun onFail(message: String) {
        getView()!!.setWorn(message)
    }

    override fun requestData(name: String, category: String, data: String, method: Method) {
        var method = method
        if (method == Method.LOCAL){
            val info = appInfoCache.infoList
            if (info == ""){
                method = Method.NET
            }else{
                onSuccess(parseData(info))
            }
        }
        if (method == Method.NET){
            NetUtil.get(Config.APP_LIST_URL, object : Listener<String>{
                override fun onSuccess(model: String) {
                    this@AppInfoListGet.onSuccess(parseData(model))
                    appInfoCache.infoList = model
                }

                override fun onFail(message: String) {
                    this@AppInfoListGet.onFail(message)
                }
            })
        }
    }

    private fun parseData(data: String): ArrayList<AppInfo>{
        val list = ArrayList<AppInfo>()
        val jsonArray = JSONArray(data)
        (0 until jsonArray.length())
                .map { jsonArray.getString(it) }
                .mapTo(list) { Gson().fromJson(it, AppInfo::class.java) }
        return list
    }
}