package com.wuruoye.all2.v3.presenter

import android.content.Context
import com.google.gson.Gson
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.v3.model.AppInfoCache
import com.wuruoye.all2.v3.model.AppList

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class AppListGet(context: Context) : AbsPresenter<AbsView<AppList>>(context), Listener<AppList> {
    private val appInfoCache = AppInfoCache(context)

    override fun onSuccess(model: AppList) {
        getView()!!.setModel(model)
    }

    override fun onFail(message: String) {
        getView()!!.setWorn(message)
    }

    override fun requestData(name: String, category: String, data: String, method: Method) {
        var method = method
        val key = name + "_" + category;
        if (method == Method.LOCAL){
            if (appInfoCache.getAppInfo(key) == ""){
                method = Method.NET
            }else{
                onSuccess(parseData(appInfoCache.getAppInfo(key)))
            }
        }
        if (method == Method.NET){
            NetUtil.get(getUrl(name, category, data), object : Listener<String>{
                override fun onSuccess(model: String) {
                    this@AppListGet.onSuccess(parseData(model))
                    appInfoCache.setAppInfo(model, key)
                }

                override fun onFail(message: String) {
                    this@AppListGet.onFail(message)
                }
            })
        }
    }

    private fun parseData(data: String): AppList =
            Gson().fromJson(data, AppList::class.java)

    private fun getUrl(name: String, category: String, data: String): String =
            Config.REMOTE_HOST + "v3/list/" + name + "/" + category + "/" + data;
}