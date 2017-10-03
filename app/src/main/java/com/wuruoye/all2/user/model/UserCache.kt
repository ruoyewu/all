package com.wuruoye.all2.user.model

import android.content.Context
import com.wuruoye.all2.base.model.BaseCache
import com.wuruoye.all2.base.model.Config

/**
 * Created by wuruoye on 2017/9/23.
 * this file is to do
 */
class UserCache(context: Context) : BaseCache(context) {

    var isLogin: Boolean
        get() = getBoolean(USER_LOGIN, false)
        set(value) = setBoolean(USER_LOGIN, value)

    var userName: String
        get() = getString(USER_NAME, "")
        set(value) = setString(USER_NAME, value)

    var userAvatar: String
        get() = getString(USER_AVATAR, "")
        set(value) = setString(USER_AVATAR, value)

    var userDesc: String
        get() = getString(USER_DESC, "")
        set(value) = setString(USER_DESC, value)

    fun cancelUser(){
        isLogin = false
        userName = ""
        userAvatar = ""
    }

    fun loginUser(name: String){
        isLogin = true
        userName = name
        userAvatar = Config.APP_PATH + "file/avatar.jpg"
    }

    fun signUser(name: String){
        isLogin = true
        userName = name
    }

    fun addReadTime(time: Long){
        var last = getLong(READ_TIME, 0L)
        last += time
        setLong(READ_TIME, last)
    }

    val readTime: Long
        get() = getLong(READ_TIME, 0L)

    companion object {
        val USER_NAME = "user_name"
        val USER_AVATAR = "user_avatar"
        val USER_LOGIN = "user_login"
        val USER_DESC = "user_desc"
        val READ_TIME = "read_time"
    }
}