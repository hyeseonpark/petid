package com.petid.data.dto.response

import com.petid.domain.entity.HospitalOrderDetailEntity
import kotlinx.serialization.Serializable

@Serializable
data class HospitalOrderDetailResponse (
    val id: Long,
    val hospitalId: Long,
    val hospitalName: String,
    val date: Long,
    val status: String
)

fun HospitalOrderDetailResponse.toDomain() = HospitalOrderDetailEntity(
    id = id,
    hospitalId = hospitalId,
    hospitalName = hospitalName,
    date = date,
    status = status
)

fun List<HospitalOrderDetailResponse>.toDomain(): List<HospitalOrderDetailEntity> {
    return this.map { it.toDomain() }
}