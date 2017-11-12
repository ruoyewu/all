package com.wuruoye.all2.user.presenter

import com.wuruoye.all2.base.presenter.BaseView
import com.wuruoye.all2.user.model.bean.ArticleFavorite

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
interface UserView : BaseView {
    fun onFavoriteGet(model: ArticleFavorite)

    fun onAvatarUpload(result: Boolean)

    fun onReadTimeUpload(result: Boolean)

    fun setWorn(message: String)
}