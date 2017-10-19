package com.wuruoye.all2.v3.presenter

import android.content.Context
import com.google.gson.Gson
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.base.util.loge
import com.wuruoye.all2.v3.model.AppInfoCache
import com.wuruoye.all2.v3.model.bean.ArticleList
import org.json.JSONArray

/**
 * Created by wuruoye on 2017/10/19.
 * this file is to do
 */
class AppListGet2(context: Context) : AbsPresenter<AbsView<ArticleList>>(), Listener<ArticleList>{
    override fun onSuccess(model: ArticleList) {
        getView()?.setModel(model)
    }

    override fun onFail(message: String) {
        getView()?.setWorn(message)
    }

    private val mAppInfoCache = AppInfoCache(context)

    fun requestArticleList(name: String, category: String, page: String, method: Method){
//        loge("requestArticleList: $name, $category, $page, $method")
        var m = method
        val key = name + "_" + category
        if (m == Method.LOCAL){
            val appInfo = mAppInfoCache.getAppInfo(key)
            if (appInfo == "") {
                m = Method.NET
            }else{
                onSuccess(Gson().fromJson(appInfo, ArticleList::class.java))
            }
        }
        if (m == Method.NET){
            val apiMap = mAppInfoCache.getApi(name)
            if (apiMap == null){
                val apiRequestUrl = Config.APP_API_URL + "name=" + name
                NetUtil.get(apiRequestUrl, object : Listener<String>{
                    override fun onSuccess(model: String) {
//                        loge(model)
                        val map = parseJson2Map(model)
                        mAppInfoCache.putApi(name, map)
                        requestWithApiMap(map, name, category, page)
                    }

                    override fun onFail(message: String) {
                        this@AppListGet2.onFail("从服务端获取api列表失败 with $name")
                    }

                })
            }else{
                requestWithApiMap(apiMap, name, category, page)
            }
        }
    }

    private fun requestWithApiMap(apiMap: HashMap<String, String>, name: String, category: String, page: String){
        val url = getUrl(apiMap, name, category, page)
//        loge(url)
        val key = name + "_" + category
        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
//                loge(model)
                val keyList = arrayListOf<String>("name", "category", "page", "content")
                val valueList = arrayListOf<String>(name, category, page, model)
                NetUtil.post(Config.ARTICLE_LIST_POST, keyList, valueList, object : Listener<String>{
                    override fun onSuccess(model: String) {
//                        loge(model)
                        val articleList = Gson().fromJson(model, ArticleList::class.java)
                        onSuccess(articleList)
                        if (page == "0"){
                            mAppInfoCache.setAppInfo(model, key)
                        }
                    }

                    override fun onFail(message: String) {
                        this@AppListGet2.onFail("从服务端获取文章列表失败 with $name")
                    }

                })
            }

            override fun onFail(message: String) {
                this@AppListGet2.onFail("api 请求有误 with $name, $category")
            }

        })
    }

    private fun parseJson2Map(json: String): HashMap<String, String>{
        val map = HashMap<String, String>()
        val array = JSONArray(json)
        for (i in 0 until array.length()){
            val obj = array.getJSONObject(i)
            val category = obj.getString("category")
            val api = obj.getString("api")
            map.put(category, api)
        }
        return map
    }

    private fun getUrl(apiMap: HashMap<String, String>, name: String, category: String, page: String): String{
//        loge("get url: $name, $category, $page")
        val regex = Regex("@page")
        var p = page
        if (p == "0"){
            if (apiMap[category + "_start"] == null){
                p = apiMap["start"]!!
            }else{
                p = apiMap[category + "_start"]!!
            }
        }

        return if (p ==  apiMap["start"]!!){
            if (apiMap[category + "_index"] == null){
                apiMap[category]!!.replace(regex, p)
            }else{
                apiMap[category + "_index"]!!
            }
        }else{
            apiMap[category]!!.replace(regex, p)
        }
    }
}