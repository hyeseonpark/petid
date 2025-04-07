package com.petid.data.dto.request

import com.petid.domain.entity.HospitalOrderEntity

data class HospitalOrderRequest(
    val hospitalId: Long,
    val date: String
)

fun HospitalOrderEntity.toDto() =
    HospitalOrderRequest(
        hospitalId = hospitalId,
        date = date,
    )