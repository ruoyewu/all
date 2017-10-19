package com.wuruoye.all2.v3.model.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
data class ArticleDetail(
        var title: String,
        var subtitle: String,
        var author: String,
        var date: String,
        val content: ArrayList<Pair>,
        val serial_list: ArrayList<String>
) {
}