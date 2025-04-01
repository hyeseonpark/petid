package com.petid.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HospitalOrderDetailEntity (
    val id: Long,
    val hospitalId: Long,
    val hospitalName: String,
    val date: Long, // Unix timestamp
    val status: String
) : Parcelable