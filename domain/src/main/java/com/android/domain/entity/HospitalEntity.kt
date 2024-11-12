package com.android.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HospitalEntity(
    val id: Int,
    val imageUrl: List<String>,
    val address: String,
    val name: String,
    val hours: String?,
    val tel: String,
    val vet: String
) : Parcelable
