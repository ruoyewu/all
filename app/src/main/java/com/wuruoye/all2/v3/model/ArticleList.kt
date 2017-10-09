package com.wuruoye.all2.v3.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
data class ArticleList(
        var name: String,
        var category: String,
        var next: String,
        var list: ArrayList<ArticleListItem>
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.createTypedArrayList(ArticleListItem.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(category)
        writeString(next)
        writeTypedList(list)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ArticleList> = object : Parcelable.Creator<ArticleList> {
            override fun createFromParcel(source: Parcel): ArticleList = ArticleList(source)
            override fun newArray(size: Int): Array<ArticleList?> = arrayOfNulls(size)
        }
    }
}