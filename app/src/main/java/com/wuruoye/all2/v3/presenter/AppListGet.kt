package com.wuruoye.all2.v3.presenter

import android.content.Context
import com.google.gson.Gson
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.v3.model.AppInfoCache
import com.wuruoye.all2.v3.model.ArticleList

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class AppListGet(context: Context) : AbsPresenter<AbsView<ArticleList>>(), Listener<ArticleList> {
    private val appInfoCache = AppInfoCache(context)

    override fun onSuccess(model: ArticleList) {
        getView()?.setModel(model)
    }

    override fun onFail(message: String) {
        getView()?.setWorn(message)
    }

    fun requestArticleList(name: String, category: String, data: String, method: Method) {
        var m = method
        val key = name + "_" + category;
        if (m == Method.LOCAL){
            if (appInfoCache.getAppInfo(key) == ""){
                m = Method.NET
            }else{
                onSuccess(parseData(appInfoCache.getAppInfo(key)))
            }
        }
        if (m == Method.NET){
            NetUtil.get(getUrl(name, category, data), object : Listener<String>{
                override fun onSuccess(model: String) {
                    this@AppListGet.onSuccess(parseData(model))
                    if (data == "0") {
                        appInfoCache.setAppInfo(model, key)
                    }
                }

                override fun onFail(message: String) {
                    this@AppListGet.onFail(message)
                }
            })
        }
    }

    private fun parseData(data: String): ArticleList =
            Gson().fromJson(data, ArticleList::class.java)

    private fun getUrl(name: String, category: String, data: String): String =
            Config.ARTICLE_LIST_URL + "name=" + name + "&category=" + category + "&page=" + data;
}