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

    var isAvatarUpload: Boolean
        get() = getBoolean(USER_AVATAR_UPLOAD, true)
        set(value) = setBoolean(USER_AVATAR_UPLOAD, value)

    var userDesc: String
        get() = getString(USER_DESC, "")
        set(value) = setString(USER_DESC, value)

    var userId: Int
        get() = getInt(USER_ID, 0)
        set(value) = setInt(USER_ID, value)

    fun cancelUser(){
        isLogin = false
        userId = 0
        userName = ""
        userAvatar = ""
    }

    fun loginUser(id: Int, name: String){
        isLogin = true
        userId = id
        userName = name
        userAvatar = Config.APP_PATH + "file/avatar.jpg"
    }

    fun signUser(id: Int, name: String){
        isLogin = true
        userId = id
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
        val USER_AVATAR_UPLOAD = "user_avatar_upload"
        val USER_LOGIN = "user_login"
        val USER_DESC = "user_desc"
        val READ_TIME = "read_time"
        val USER_ID = "user_id"
    }
}