package com.wuruoye.all2.v3.model.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2017/10/19.
 * this file is to do
 */
data class Pair(
        val type: String,
        val info: String
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Pair> = object : Parcelable.Creator<Pair> {
            override fun createFromParcel(source: Parcel): Pair = Pair(source)
            override fun newArray(size: Int): Array<Pair?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    source.readString(),
    source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(type)
        dest.writeString(info)
    }
}