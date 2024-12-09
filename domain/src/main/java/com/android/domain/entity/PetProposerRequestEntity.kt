package com.android.domain.entity

data class PetProposerRequestEntity(
    val name: String,
    val address: String,
    val addressDetails: String,
    val rra: String,
    val rraDetails: String,
    val phone: String,
)