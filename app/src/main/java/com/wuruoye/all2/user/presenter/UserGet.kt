package com.wuruoye.all2.user.presenter

import com.google.gson.Gson
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.user.model.bean.ArticleFavorite

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
class UserGet : AbsPresenter<UserView>() {

    fun getFavorite(username: String, time: Long){
        val url = Config.FAVORITE_GET_URL + "username=" + username + "&time=" + time

        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
//                val jsonObject = JSONObject(model)
//                val result = jsonObject.getBoolean("result")
//                val next = jsonObject.getLong("next")
//                val info = jsonObject.getJSONArray("info")
//                val list = ArrayList<ArticleListItem>()
//                loge(result)
//                loge(next)
//                loge(info)
//                loge(info.length())
//                for (i in 0 until info.length()){
//                    val itemString = info[i].toString()
//                    val item = Gson().fromJson(itemString, ArticleListItem::class.java)
//                    list.add(item)
//                }
//                getView()?.onFavoriteGet(ArticleFavorite(result, next, list))
                getView()?.onFavoriteGet(Gson().fromJson(model, ArticleFavorite::class.java))
            }

            override fun onFail(message: String) {
                getView()?.setWorn(message)
            }

        })
    }
}