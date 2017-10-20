package com.wuruoye.all2.user.model.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2017/9/27.
 * this file is to do
 */
data class ArticleFavoriteItem(
        var userid: Int,
        var info: String,
        var time: Long,
        var key: String
)