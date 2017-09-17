package com.wuruoye.all2.v3.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2017/9/15.
 * this file is to do
 */
data class AppInfo(
        var name: String,
        var title: String,
        var icon: String,
        var category_name: ArrayList<String>,
        var category_title: ArrayList<String>
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.createStringArrayList(),
            source.createStringArrayList()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(title)
        writeString(icon)
        writeStringList(category_name)
        writeStringList(category_title)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<AppInfo> = object : Parcelable.Creator<AppInfo> {
            override fun createFromParcel(source: Parcel): AppInfo = AppInfo(source)
            override fun newArray(size: Int): Array<AppInfo?> = arrayOfNulls(size)
        }
    }
}