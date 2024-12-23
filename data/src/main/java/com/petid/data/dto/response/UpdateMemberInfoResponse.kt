package com.petid.data.dto.response

import com.petid.domain.entity.UpdateMemberInfoEntity


data class UpdateMemberInfoResponse(
    val name: String,
    val address: String,
    val addressDetails: String,
    val phone: String
)

fun UpdateMemberInfoResponse.toDomain() = UpdateMemberInfoEntity(
    name = name,
    address = address,
    addressDetails = addressDetails,
    phone = phone
)
