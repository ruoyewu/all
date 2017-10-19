package com.wuruoye.all2.v3.model.bean

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
        var img_list: ArrayList<String>,
        var other_info: String,
        var open_type: String,
        var content: ArrayList<Pair>
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ArticleListItem> = object : Parcelable.Creator<ArticleListItem> {
            override fun createFromParcel(source: Parcel): ArticleListItem = ArticleListItem(source)
            override fun newArray(size: Int): Array<ArticleListItem?> = arrayOfNulls(size)
        }
    }

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
    source.createStringArrayList(),
    source.readString(),
    source.readString(),
    ArrayList<Pair>().apply { source.readList(this, Pair::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(title)
        dest.writeString(forward)
        dest.writeString(author)
        dest.writeString(image)
        dest.writeString(video)
        dest.writeString(date)
        dest.writeString(time_millis)
        dest.writeString(age)
        dest.writeString(original_url)
        dest.writeString(type)
        dest.writeString(category_id)
        dest.writeStringList(img_list)
        dest.writeString(other_info)
        dest.writeString(open_type)
        dest.writeList(content)
    }
}