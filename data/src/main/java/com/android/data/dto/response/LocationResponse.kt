package com.android.data.dto.response

import com.android.domain.entity.LocationEntity
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

fun List<LocationResponse>.toDomain(): List<LocationEntity> {
    return this.map { it.toDomain() }
}