package com.petid.domain.entity

data class UpdateMemberInfoEntity(
    val name: String,
    val address: String,
    val addressDetails: String,
    val phone: String
)