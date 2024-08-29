package com.android.data.model

import com.android.domain.entity.LocationEntity
import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)

fun LocationResponse.toDomain() = LocationEntity(
    id = id,
    name = name
)