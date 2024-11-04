package com.android.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HospitalOrderDetailEntity (
    val id: Int,
    val hospitalName: String,
    val date: Long, // Unix timestamp
    val status: String
) : Parcelable