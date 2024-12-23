package com.petid.data.dto.response

import com.petid.domain.entity.HospitalOrderEntity

data class HospitalOrderResponse(
    val hospitalId: Int,
    val date: String
)

fun HospitalOrderResponse.toDomain() = HospitalOrderEntity(
    hospitalId = hospitalId,
    date = date
)
