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

    fun getFavorite(userid: Int, time: Long){
        val url = Config.FAVORITE_GET_URL + "userid=" + userid + "&time=" + time

        NetUtil.get(url, object : Listener<String>{
            override fun onSuccess(model: String) {
                getView()?.onFavoriteGet(Gson().fromJson(model, ArticleFavorite::class.java))
            }

            override fun onFail(message: String) {
                getView()?.setWorn(message)
            }

        })
    }

    fun uploadAvatar(userid: Int, filePath: String){
        val fileType = "image/*"
        val keyList = arrayListOf<String>("userid")
        val valueList = arrayListOf<String>(userid.toString())
        NetUtil.postFile(Config.USER_AVATAR_URL, filePath, fileType, keyList, valueList, object : Listener<String>{
            override fun onSuccess(model: String) {
                getView()?.onAvatarUpload(true)
            }

            override fun onFail(message: String) {
                getView()?.onAvatarUpload(false)
            }

        })
    }
}