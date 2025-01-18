package com.petid.data.dto.request

import com.petid.domain.entity.PetProposer

data class PetProposerRequest(
    val name: String,
    val address: String,
    val addressDetails: String,
    val rra: String,
    val rraDetails: String,
    val phone: String,
)

fun PetProposer.toDto() = PetProposerRequest(
    name = name,
    address = address,
    addressDetails = addressDetails,
    rra = rra,
    rraDetails = rraDetails,
    phone = phone,
)
