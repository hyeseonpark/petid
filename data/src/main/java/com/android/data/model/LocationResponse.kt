package com.android.data.model

import com.android.domain.entity.LocationEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LocationResponse(
    val id: Int,
    val name: String
)

fun LocationResponse.toDomain() = LocationEntity(
    id = id,
    name = name
)