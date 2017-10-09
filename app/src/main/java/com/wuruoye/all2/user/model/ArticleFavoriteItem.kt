package com.wuruoye.all2.user.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2017/9/27.
 * this file is to do
 */
data class ArticleFavoriteItem(
        var username: String,
        var info: String,
        var time: Long,
        var key: String
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readLong(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(username)
        writeString(info)
        writeLong(time)
        writeString(key)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ArticleFavoriteItem> = object : Parcelable.Creator<ArticleFavoriteItem> {
            override fun createFromParcel(source: Parcel): ArticleFavoriteItem = ArticleFavoriteItem(source)
            override fun newArray(size: Int): Array<ArticleFavoriteItem?> = arrayOfNulls(size)
        }
    }
}