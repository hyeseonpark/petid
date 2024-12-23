package com.petid.data.dto.request

data class UpdateMemberInfoRequest(
    val address: String,
    val addressDetails: String,
    val phone: String
)