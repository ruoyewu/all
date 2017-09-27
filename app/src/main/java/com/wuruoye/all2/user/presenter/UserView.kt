package com.wuruoye.all2.user.presenter

import com.wuruoye.all2.base.presenter.BaseView

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
interface UserView : BaseView {
    fun onLogin()

    fun onLogout()

    fun onSignIn()

    fun onCollectGet()

    fun onUserInfoPut()

    fun onUserInfoGet()

    fun onUserCoursePut()

    fun onUserCourseGet()
}