package com.wuruoye.all2.v3.presenter

import com.google.gson.Gson
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.v3.model.ArticleInfo

/**
 * Created by wuruoye on 2017/9/22.
 * this file is to do
 */
class ArticleInfoGet : AbsPresenter<AbsView<ArticleInfo>>(), Listener<ArticleInfo> {
    override fun onSuccess(model: ArticleInfo) {
        getView()?.setModel(model)
    }

    override fun onFail(message: String) {
        getView()?.setWorn(message)
    }

    public fun getArticleInfo(key: String, username: String){
        val url = Config.ARTICLE_INFO_URL + "key=" + key + "&username=" + username
        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
                this@ArticleInfoGet.onSuccess(parseData(model))
            }

            override fun onFail(message: String) {
                this@ArticleInfoGet.onFail(message)
            }

        })
    }

    private fun parseData(data: String): ArticleInfo =
            Gson().fromJson(data, ArticleInfo::class.java)

    override fun requestData(name: String, category: String, data: String, method: Method) {
    }
}