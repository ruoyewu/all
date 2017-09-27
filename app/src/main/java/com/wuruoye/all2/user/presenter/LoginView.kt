package com.wuruoye.all2.user.presenter

import com.wuruoye.all2.base.presenter.BaseView

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
interface LoginView : BaseView {

    fun onLogin(result: Boolean, info: String)

    fun onSign(result: Boolean, info: String)

    fun setWorn(message: String)
}