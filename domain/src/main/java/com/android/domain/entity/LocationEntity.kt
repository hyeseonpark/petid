package com.android.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationEntity(
    val id: Int,
    val name: String
) : Parcelable
