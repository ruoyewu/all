package com.wuruoye.all2.v3.model.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2017/9/21.
 * this file is to do
 */
data class ArticleCommentItem(
        var id: Int,
        var time: Long,
        var username: String,
        var content: String,
        var key: String,
        var parent: String
) {
}