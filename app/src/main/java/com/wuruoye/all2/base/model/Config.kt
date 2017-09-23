package com.wuruoye.all2.base.model

import android.os.RemoteException

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
object Config {
    private val REMOTE_HOST = "http://139.199.153.45/"
    val APP_LIST_URL = "https://raw.githubusercontent.com/ruoyewu/repository/master/all/v3_all.json"

    val ARTICLE_LIST_URL = REMOTE_HOST + "v3/article/list?"
    val ARTICLE_DETAIL_URL = REMOTE_HOST + "v3/article/detail?"
    val ARTICLE_INFO_URL = REMOTE_HOST + "v3/article/info?"

    val COMMENT_GET_URL = REMOTE_HOST + "v3/comment/get?"
    val COMMENT_PUT_URL = REMOTE_HOST + "v3/comment/add"
    val COMMENT_DELETE_URL = REMOTE_HOST + "v3/comment/delete?"

    val ARTICLE_LOVE_URL = REMOTE_HOST + "v3/article/love?"

    val CONNECT_TIME_OUT = 10L
    val READ_TIME_OUT = 30L
}