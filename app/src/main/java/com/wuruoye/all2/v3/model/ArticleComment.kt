package com.wuruoye.all2.v3.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2017/9/21.
 * this file is to do
 */
data class ArticleComment(
        var result: Boolean,
        var next: Long,
        var info: String,
        var list: ArrayList<ArticleCommentItem>
) : Parcelable {
    constructor(source: Parcel) : this(
            1 == source.readInt(),
            source.readLong(),
            source.readString(),
            ArrayList<ArticleCommentItem>().apply { source.readList(this, ArticleCommentItem::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (result) 1 else 0))
        writeLong(next)
        writeString(info)
        writeList(list)
    }

    companion object {
        fun getNullCommentList(): ArticleComment =
                ArticleComment(true, 0, "", arrayListOf())

        @JvmField
        val CREATOR: Parcelable.Creator<ArticleComment> = object : Parcelable.Creator<ArticleComment> {
            override fun createFromParcel(source: Parcel): ArticleComment = ArticleComment(source)
            override fun newArray(size: Int): Array<ArticleComment?> = arrayOfNulls(size)
        }
    }
}