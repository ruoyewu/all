package com.wuruoye.all2.base.model

import android.os.RemoteException

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
object Config {
    val REMOTE_HOST = "http://139.199.153.45/"
    val APP_LIST_URL = "https://raw.githubusercontent.com/ruoyewu/repository/master/all/v3_all.json"

    val COMMENT_GET_URL = REMOTE_HOST + "v3/comment/get?"
    val COMMENT_PUT_URL = REMOTE_HOST + "v3/comment/add?"

    val CONNECT_TIME_OUT = 10L
    val READ_TIME_OUT = 30L
}