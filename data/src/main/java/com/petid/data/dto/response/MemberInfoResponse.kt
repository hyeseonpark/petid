package com.petid.data.dto.response

import com.petid.domain.entity.MemberInfoEntity

data class MemberInfoResponse (
    val memberId: Int,
    val name: String,
    val address: String?,
    val addressDetails: String?,
    val phone: String?,
    val image: String?,
    val petId: Int?
)

fun MemberInfoResponse.toDomain() = MemberInfoEntity(
    memberId = memberId,
    name = name,
    address = address,
    addressDetails = addressDetails,
    phone = phone,
    image = image,
    petId = petId
)
