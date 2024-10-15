package com.android.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HospitalOrderDetailEntity (
    val id: Int,
    val uid: String,
    val hospitalId: Int,
    val date: Long, // Unix timestamp
    val status: String
) : Parcelable