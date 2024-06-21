package com.dicoding.mybuah

import android.os.Parcel
import android.os.Parcelable

data class Tips(
    val judul: String?,
    val description: String?,
    val gambar: Int,
    val tanggal: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(judul)
        parcel.writeString(description)
        parcel.writeInt(gambar)
        parcel.writeString(tanggal)
    }

    companion object CREATOR : Parcelable.Creator<Tips> {
        override fun createFromParcel(parcel: Parcel): Tips {
            return Tips(parcel)
        }

        override fun newArray(size: Int): Array<Tips?> {
            return arrayOfNulls(size)
        }
    }
}
