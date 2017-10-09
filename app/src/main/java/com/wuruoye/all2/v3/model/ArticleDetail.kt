package com.wuruoye.all2.v3.model

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
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.createTypedArrayList(Pair.CREATOR),
            source.createStringArrayList()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(subtitle)
        writeString(author)
        writeString(date)
        writeTypedList(content)
        writeStringList(serial_list)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ArticleDetail> = object : Parcelable.Creator<ArticleDetail> {
            override fun createFromParcel(source: Parcel): ArticleDetail = ArticleDetail(source)
            override fun newArray(size: Int): Array<ArticleDetail?> = arrayOfNulls(size)
        }
    }
}