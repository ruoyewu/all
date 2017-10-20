package com.wuruoye.all2.v3.presenter

import com.google.gson.Gson
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.base.util.loge
import com.wuruoye.all2.v3.model.bean.ArticleComment
import com.wuruoye.all2.v3.model.bean.ArticleCommentItem
import com.wuruoye.all2.v3.model.bean.ArticleDetail
import com.wuruoye.all2.v3.model.bean.ArticleInfo
import org.json.JSONObject

/**
 * Created by wuruoye on 2017/9/23.
 * this file is to do
 */
class ArticleGet : AbsPresenter<ArticleView>() {

    fun getArticleDetail(name: String, category: String, id: String){
        val url = Config.ARTICLE_DETAIL_URL + "name=" + name + "&category=" + category + "&id=" + id
        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
                try {
                    getView()?.onArticleDetail(
                            Gson().fromJson(model, ArticleDetail::class.java)
                    )
                } catch (e: Exception) {
                    getView()?.setWorn(e.message!!)
                }
            }

            override fun onFail(message: String) {
                getView()?.setWorn(message)
            }
        })
    }

    fun getArticleInfo(key: String, userid: Int){
        val url = Config.ARTICLE_INFO_URL + "key=" + key + "&userid=" + userid
        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
                getView()?.onArticleInfo(
                        Gson().fromJson(model, ArticleInfo::class.java)
                )
            }

            override fun onFail(message: String) {
                getView()?.setWorn(message)
            }

        })
    }

    fun getCommentList(key: String, time: Long){
        val url = Config.COMMENT_GET_URL + "key=" + key + "&time=" + time
        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
                val commentResult = Gson().fromJson(model, ArticleComment::class.java)
                if (commentResult.result){
                    getView()?.onCommentGet(commentResult)
                }else{
                    getView()?.setWorn(commentResult.info)
                }
            }

            override fun onFail(message: String) {
                getView()?.setWorn(message)
            }

        })
    }

    fun putComment(time: Long, userid: Int, content: String, key: String, parent: Int){
        val url = Config.COMMENT_PUT_URL
        val keyList = arrayListOf("time", "userid", "content", "key", "parent")
        val valueList = arrayListOf(time.toString(), userid.toString(), content, key, parent.toString())
        NetUtil.post(url, keyList, valueList, object : Listener<String>{
            override fun onSuccess(model: String) {
                val jsonObject = JSONObject(model)
                val result = jsonObject.getBoolean("result")
                val info = jsonObject.getString("info")
                if (result){
                    getView()?.onCommentPut(
                            Gson().fromJson(info, ArticleCommentItem::class.java)
                    )
                }else{
                    getView()?.setWorn(info)
                }
            }

            override fun onFail(message: String) {
                getView()?.setWorn(message)
            }

        })
    }

    fun deleteComment(id: Int){
        val url = Config.COMMENT_DELETE_URL + "id=" + id
        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
                val jsonObject = JSONObject(model)
                getView()?.onCommentDelete(jsonObject.getBoolean("result"))
            }

            override fun onFail(message: String) {
                getView()?.setWorn(message)
            }

        })
    }

    fun putLove(key: String, userid: Int, love: Boolean){
        val url = Config.ARTICLE_LOVE_URL + "key=" + key + "&userid=" + userid + "&love=" +
                if (love){
                    1
                }else{
                    0
                }
        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
                val jsonObject = JSONObject(model)
                val result = jsonObject.getBoolean("result")
                getView()?.onLovePut(result)
            }

            override fun onFail(message: String) {
                getView()?.setWorn(message)
            }

        })
    }

    fun putFavorite(key: String, userid: Int, info: String, time: Long, favorite: Boolean){
        val url = Config.FAVORITE_PUT_URL
        val keyList = arrayListOf("key", "userid", "info", "time", "favorite")
        val valueList = arrayListOf(key, userid.toString(), info, time.toString(), if (favorite) "1" else "0")
        loge(info)

        NetUtil.post(url, keyList, valueList, object : Listener<String>{
            override fun onSuccess(model: String) {
                val jsonObject = JSONObject(model)
                val result = jsonObject.getBoolean("result")
                getView()?.onFavoritePut(result)
            }

            override fun onFail(message: String) {
                getView()?.setWorn(message)
            }

        })
    }
}