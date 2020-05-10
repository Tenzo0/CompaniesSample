/*
 * Copyright (c) Barykin Alexey, 2020
 */

package com.example.application.domain

import android.os.Parcel
import android.os.Parcelable

data class DetailedCompanyItem (
    val id: Long,
    val name: String,
    val img: String,
    val description: String,
    val lat: Double,
    val lon: Double,
    val www: String,
    val phone: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(img)
        parcel.writeString(description)
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
        parcel.writeString(www)
        parcel.writeString(phone)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetailedCompanyItem> {
        override fun createFromParcel(parcel: Parcel): DetailedCompanyItem {
            return DetailedCompanyItem(parcel)
        }

        override fun newArray(size: Int): Array<DetailedCompanyItem?> {
            return arrayOfNulls(size)
        }
    }
}