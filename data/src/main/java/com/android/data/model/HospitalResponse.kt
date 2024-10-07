package com.android.data.model

import com.android.domain.entity.HospitalEntity
import kotlinx.serialization.Serializable

@Serializable
data class HospitalResponse(
    val id: Int,
    val imageUrl: List<String>,
    val address: String,
    val name: String,
    val hours: String?,
    val tel: String,
    val vet: String
)

fun HospitalResponse.toDomain() = HospitalEntity(
    id = id,
    imageUrl = imageUrl,
    address = address,
    name = name,
    hours = hours,
    tel = tel,
    vet = vet
)

fun List<HospitalResponse>.toDomain(): List<HospitalEntity> {
    return this.map { it.toDomain() }
}