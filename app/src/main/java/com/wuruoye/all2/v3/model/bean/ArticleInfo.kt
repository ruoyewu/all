package com.wuruoye.all2.v3.model.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2017/9/22.
 * this file is to do
 */
data class ArticleInfo(
        var result: Boolean,
        var love: Int,
        var comment: Int,
        var favorite: Boolean
) {
}