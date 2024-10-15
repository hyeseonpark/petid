package com.android.data.dto.response

import com.android.domain.entity.HospitalOrderDetailEntity
import kotlinx.serialization.Serializable

@Serializable
data class HospitalOrderDetailResponse (
    val id: Int,
    val uid: String,
    val hospitalId: Int,
    val date: Long,
    val status: String
)

fun HospitalOrderDetailResponse.toDomain() = HospitalOrderDetailEntity(
    id = id,
    uid = uid,
    hospitalId = hospitalId,
    date = date,
    status = status
)

fun List<HospitalOrderDetailResponse>.toDomain(): List<HospitalOrderDetailEntity> {
    return this.map { it.toDomain() }
}