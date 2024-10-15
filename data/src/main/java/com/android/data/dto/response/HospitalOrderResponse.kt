package com.android.data.dto.response

import com.android.domain.entity.HospitalOrderEntity

data class HospitalOrderResponse(
    val hospitalId: Int,
    val date: String
)

fun HospitalOrderResponse.toDomain() = HospitalOrderEntity(
    hospitalId = hospitalId,
    date = date
)
