package com.wuruoye.all2.v3.presenter

import android.content.Context
import com.google.gson.Gson
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.v3.model.AppInfoCache
import com.wuruoye.all2.v3.model.ArticleDetail
import com.wuruoye.all2.base.util.extensions.loge

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
class ArticleDetailGet(context: Context) : AbsPresenter<AbsView<ArticleDetail>>(context), Listener<ArticleDetail> {
    private val appInfoCache = AppInfoCache(context)

    override fun onSuccess(model: ArticleDetail) {
        getView()?.setModel(model)
    }

    override fun onFail(message: String) {
        getView()?.setWorn(message)
    }

    override fun requestData(name: String, category: String, data: String, method: Method) {
        val key = name + "_" + category + "_" + data
        var method = method
        if (method == Method.LOCAL) {
//            loge(appInfoCache.getArticleDetail(key))
//            if (appInfoCache.getArticleDetail(key) == ""){
//                method = Method.NET
//            }else{
//                onSuccess(parseData(data))
//            }
            method = Method.NET
        }
        if (method == Method.NET){
            NetUtil.get(getUrl(name, category, data), object : Listener<String>{
                override fun onSuccess(model: String) {
//                    loge(model)
                    this@ArticleDetailGet.onSuccess(parseData(model))
                    appInfoCache.setArticleDetail(model, key)
                }

                override fun onFail(message: String) {
                    this@ArticleDetailGet.onFail(message)
                }

            })
        }
    }

    private fun parseData(data: String): ArticleDetail =
            Gson().fromJson(data, ArticleDetail::class.java)

    private fun getUrl(name: String, category: String, data: String): String =
            Config.REMOTE_HOST + "v3/detail/" + name + "/" + category + "/" + data

}