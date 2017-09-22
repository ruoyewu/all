package com.wuruoye.all2.v3.presenter

import android.content.Context
import com.google.gson.Gson
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.v3.model.ArticleComment
import com.wuruoye.all2.v3.model.ArticleCommentItem

/**
 * Created by wuruoye on 2017/9/21.
 * this file is to do
 */
class ArticleCommentGet(context: Context) : AbsPresenter<AbsView<ArticleComment>>(), Listener<ArticleComment>{

    override fun onSuccess(model: ArticleComment) {
        getView()?.setModel(model)
    }

    override fun onFail(message: String) {
        getView()?.setWorn(message)
    }

    fun requestComment(key: String, time: Long){
        val url = Config.COMMENT_GET_URL + "key=" + key + "&time=" + time
        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
                try {
                    val comment = parseData(model)
                    if (comment.result){
                        this@ArticleCommentGet.onSuccess(parseData(model))
                    }else{
                        this@ArticleCommentGet.onFail(comment.info)
                    }
                } catch (e: Exception) {
                    this@ArticleCommentGet.onFail(e.message!!)
                }
            }

            override fun onFail(message: String) {
                this@ArticleCommentGet.onFail(message)
            }

        })
    }

    private fun parseData(data: String): ArticleComment =
            Gson().fromJson(data, ArticleComment::class.java)

    override fun requestData(name: String, category: String, data: String, method: Method) {

    }
}