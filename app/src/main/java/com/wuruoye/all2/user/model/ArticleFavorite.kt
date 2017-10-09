package com.wuruoye.all2.user.model

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
) : Parcelable {
    constructor() : this(true, 0, ArrayList())

    constructor(source: Parcel) : this(
            1 == source.readInt(),
            source.readLong(),
            ArrayList<ArticleFavoriteItem>().apply { source.readList(this, ArticleFavoriteItem::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (result) 1 else 0))
        writeLong(next)
        writeList(info)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ArticleFavorite> = object : Parcelable.Creator<ArticleFavorite> {
            override fun createFromParcel(source: Parcel): ArticleFavorite = ArticleFavorite(source)
            override fun newArray(size: Int): Array<ArticleFavorite?> = arrayOfNulls(size)
        }
    }
}