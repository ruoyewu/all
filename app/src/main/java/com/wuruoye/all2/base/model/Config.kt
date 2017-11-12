package com.wuruoye.all2.base.model

import android.Manifest
import android.os.Environment
import android.os.RemoteException

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
object Config {
    private val REMOTE_HOST = "https://all.wuruoye.com/"
//    private val REMOTE_HOST = "http://139.199.153.45:3422/"
    val APP_LIST_URL = "https://git.whutdev.com/wuruoye/repository/raw/master/all/app.json"

    val APP_API_URL = REMOTE_HOST + "v3/app_api?"

    val USER_LOGIN_URL = REMOTE_HOST + "v3/user_login?"
    val USER_SIGN_URL = REMOTE_HOST + "v3/user_sign"
    val USER_AVATAR_URL = REMOTE_HOST + "v3/user/avatar"
    val USER_AVATAR_UPLOAD = REMOTE_HOST + "v3/user_avatar"
    val USER_READ_TIME_UPLOAD = REMOTE_HOST + "v3/user_read_time"

    val ARTICLE_LIST_URL = REMOTE_HOST + "v3/article_list2"
    val ARTICLE_DETAIL_URL = REMOTE_HOST + "v3/article_detail?"
    val ARTICLE_INFO_URL = REMOTE_HOST + "v3/article_info?"

    val FAVORITE_PUT_URL = REMOTE_HOST + "v3/favorite_put"
    val FAVORITE_GET_URL = REMOTE_HOST + "v3/favorite_get?"

    val COMMENT_GET_URL = REMOTE_HOST + "v3/comment_get?"
    val COMMENT_PUT_URL = REMOTE_HOST + "v3/comment_add"
    val COMMENT_DELETE_URL = REMOTE_HOST + "v3/comment_delete"
    val COMMENT_LOVE_URL = REMOTE_HOST + "v3/comment_love"

    val ARTICLE_LOVE_URL = REMOTE_HOST + "v3/article_love"

    val CONNECT_TIME_OUT = 30L
    val READ_TIME_OUT = 30L

    val APP_PATH = Environment.getExternalStorageDirectory().absolutePath + "/com.wuruoye.all2/"
    val FILE_PATH = APP_PATH + "file/"
    val IMAGE_PATH = Environment.getExternalStorageDirectory().absolutePath + "/DCIM/Camera/"
    val PROVIDER_AUTHORITY = "com.wuruoye.all2.fileprovider"
    val FILE_PERMISSION = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val CAMERA_PERMISSION = arrayOf(
            Manifest.permission.CAMERA
    )

    val PUBLIC_RSA_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZUR+ZVrweku0atvEePdp/vDFP\n" +
            "PswzyqPFUJU0TdW6Fj+eLbmYEfjrJkDjZhcoI9MupdP3xbQIkKozlnzNUXQW0j77\n" +
            "h0BLToEEe7gNdN3Ro/QTmE4NxVfkXSBV4GuWNriBzzgratUvAP5K7ZC6hPJr/+a4\n" +
            "sPrBN2SLwIjuPnTU/wIDAQAB\n"
}