package com.android.data.dto.response

import com.android.domain.entity.LocationEntity
import com.android.domain.entity.MemberInfoEntity

data class MemberInfoResponse (
    val name: String,
    val address: String?,
    val addressDetails: String?,
    val phone: String?,
    val image: String?,
    val petId: Int?
)

fun MemberInfoResponse.toDomain() = MemberInfoEntity(
    name = name,
    address = address,
    addressDetails = addressDetails,
    phone = phone,
    image = image,
    petId = petId
)
