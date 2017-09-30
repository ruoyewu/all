package com.wuruoye.all2.base.model

import android.Manifest
import android.os.Environment
import android.os.RemoteException

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
object Config {
    private val REMOTE_HOST = "http://139.199.153.45/"
    val APP_LIST_URL = "https://raw.githubusercontent.com/ruoyewu/repository/master/all/v3_all.json"

    val USER_LOGIN_URL = REMOTE_HOST + "v3/user/login?"
    val USER_SIGN_URL = REMOTE_HOST + "v3/user/sign?"
    val USER_AVATAR_URL = REMOTE_HOST + "v3/user/avatar"

    val ARTICLE_LIST_URL = REMOTE_HOST + "v3/article/list?"
    val ARTICLE_DETAIL_URL = REMOTE_HOST + "v3/article/detail?"
    val ARTICLE_INFO_URL = REMOTE_HOST + "v3/article/info?"

    val FAVORITE_PUT_URL = REMOTE_HOST + "v3/favorite/put"
    val FAVORITE_GET_URL = REMOTE_HOST + "v3/favorite/get?"

    val COMMENT_GET_URL = REMOTE_HOST + "v3/comment/get?"
    val COMMENT_PUT_URL = REMOTE_HOST + "v3/comment/add"
    val COMMENT_DELETE_URL = REMOTE_HOST + "v3/comment/delete?"

    val ARTICLE_LOVE_URL = REMOTE_HOST + "v3/article/love?"

    val CONNECT_TIME_OUT = 30L
    val READ_TIME_OUT = 30L

    val APP_PATH = Environment.getExternalStorageDirectory().absolutePath + "/com.wuruoye.all2/"
    val FILE_PATH = APP_PATH + "file/"
    val IMAGE_PATH = APP_PATH + "image/"
    val PROVIDER_AUTHORITY = "com.wuruoye.all2.fileprovider"
    val FILE_PERMISSION = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val CAMERA_PERMISSION = arrayOf(
            Manifest.permission.CAMERA
    )
}