package com.android.data.model

data class HospitalOrderDetailResponse (
    val id: Int,
    val uid: String,
    val hospitalId: Int,
    val date: Long,
    val status: String
)

fun HospitalOrderDetailResponse.toDomain() = HospitalOrderDetailResponse(
    id = id,
    uid = uid,
    hospitalId = hospitalId,
    date = date,
    status = status
)