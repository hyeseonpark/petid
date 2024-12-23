package com.petid.domain.entity

data class PetProposerRequestEntity(
    val name: String,
    val address: String,
    val addressDetails: String,
    val rra: String,
    val rraDetails: String,
    val phone: String,
)