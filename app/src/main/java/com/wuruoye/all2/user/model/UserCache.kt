package com.wuruoye.all2.user.model

import android.content.Context
import com.wuruoye.all2.base.model.BaseCache

/**
 * Created by wuruoye on 2017/9/23.
 * this file is to do
 */
class UserCache(context: Context) : BaseCache(context) {

    var isLogin: Boolean
        get() = getBoolean(USER_LOGIN, true)
        set(value) = setBoolean(USER_LOGIN, value)

    var userName: String
        get() = getString(USER_NAME, "ruoye")
        set(value) = setString(USER_NAME, value)

    var userAvatar: String
        get() = getString(USER_AVATAR, "")
        set(value) = setString(USER_AVATAR, value)

    companion object {
        val USER_NAME = "user_name"
        val USER_AVATAR = "user_avatar"
        val USER_LOGIN = "user_login"

    }
}