package com.wuruoye.all2.v3.model

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
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeLong(time)
        writeString(username)
        writeString(content)
        writeString(key)
        writeString(parent)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ArticleCommentItem> = object : Parcelable.Creator<ArticleCommentItem> {
            override fun createFromParcel(source: Parcel): ArticleCommentItem = ArticleCommentItem(source)
            override fun newArray(size: Int): Array<ArticleCommentItem?> = arrayOfNulls(size)
        }
    }
}