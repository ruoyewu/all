package com.wuruoye.all2.user.presenter

import com.wuruoye.all2.base.presenter.BaseView
import com.wuruoye.all2.user.model.ArticleFavorite

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
interface UserView : BaseView {
    fun onFavoriteGet(model: ArticleFavorite)

    fun setWorn(message: String)
}