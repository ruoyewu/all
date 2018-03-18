package com.wuruoye.all2.setting.model.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wuruoye on 2018/3/7.
 * this file is to
 */
data class ManageredApp (
        var icon: String,
        var title: String,
        var isChecked: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(icon)
        parcel.writeString(title)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ManageredApp> {
        override fun createFromParcel(parcel: Parcel): ManageredApp {
            return ManageredApp(parcel)
        }

        override fun newArray(size: Int): Array<ManageredApp?> {
            return arrayOfNulls(size)
        }
    }
}