package com.wuruoye.all2.v3.model

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
) : Parcelable {
    constructor(source: Parcel) : this(
            1 == source.readInt(),
            source.readInt(),
            source.readInt(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (result) 1 else 0))
        writeInt(love)
        writeInt(comment)
        writeInt((if (favorite) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ArticleInfo> = object : Parcelable.Creator<ArticleInfo> {
            override fun createFromParcel(source: Parcel): ArticleInfo = ArticleInfo(source)
            override fun newArray(size: Int): Array<ArticleInfo?> = arrayOfNulls(size)
        }
    }
}