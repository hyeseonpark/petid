package com.android.data.dto.response

import com.android.domain.entity.LocationEntity
import com.android.domain.entity.MemberInfoEntity

data class MemberInfoResponse (
    val name: String,
    val address: String,
    val phone: String,
    val image: String?
)

fun MemberInfoResponse.toDomain() = MemberInfoEntity(
    name = name,
    address = address,
    phone = phone,
    image = image,
)
