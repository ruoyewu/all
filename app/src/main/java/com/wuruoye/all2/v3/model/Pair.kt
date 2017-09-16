package com.wuruoye.all2.v3.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2017/9/16.
 * this file is to do
 */
data class Pair(
        var type: String,
        var info: String
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(type)
        writeString(info)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Pair> = object : Parcelable.Creator<Pair> {
            override fun createFromParcel(source: Parcel): Pair = Pair(source)
            override fun newArray(size: Int): Array<Pair?> = arrayOfNulls(size)
        }
    }
}