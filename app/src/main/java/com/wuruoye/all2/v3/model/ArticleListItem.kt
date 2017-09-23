package com.wuruoye.all2.v3.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
data class ArticleListItem(
        var id: String,
        var title: String,
        var forward: String,
        var author: String,
        var image: String,
        var video: String,
        var date: String,
        var time_millis: String,
        var age: String,
        var original_url: String,
        var type: String,
        var category_id: String,
        var other_info: String,
        var open_type: String,
        var content: ArrayList<Pair>
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.createTypedArrayList(Pair.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(title)
        writeString(forward)
        writeString(author)
        writeString(image)
        writeString(video)
        writeString(date)
        writeString(time_millis)
        writeString(age)
        writeString(original_url)
        writeString(type)
        writeString(category_id)
        writeString(other_info)
        writeString(open_type)
        writeTypedList(content)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ArticleListItem> = object : Parcelable.Creator<ArticleListItem> {
            override fun createFromParcel(source: Parcel): ArticleListItem = ArticleListItem(source)
            override fun newArray(size: Int): Array<ArticleListItem?> = arrayOfNulls(size)
        }
    }
}