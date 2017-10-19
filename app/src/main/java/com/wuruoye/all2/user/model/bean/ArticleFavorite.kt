package com.wuruoye.all2.user.model.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2017/9/27.
 * this file is to do
 */
data class ArticleFavorite(
        var result: Boolean,
        var next: Long,
        var info: ArrayList<ArticleFavoriteItem>
){
    constructor(): this(false, 0L, ArrayList())
}