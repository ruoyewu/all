package com.wuruoye.all2.user.presenter

import com.google.gson.Gson
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.presenter.AbsPresenter
import com.wuruoye.all2.base.presenter.AbsView
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.user.model.ArticleFavorite

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
class UserGet : AbsPresenter<UserView>() {

    fun getFavorite(username: String, time: Long){
        val url = Config.FAVORITE_GET_URL + "username=" + username + "&time=" + time

        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
                getView()?.onFavoriteGet(Gson().fromJson(model, ArticleFavorite::class.java))
            }

            override fun onFail(message: String) {
                getView()?.setWorn(message)
            }

        })
    }
}