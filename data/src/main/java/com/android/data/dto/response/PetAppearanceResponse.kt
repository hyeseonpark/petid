package com.android.data.dto.response

data class PetAppearanceResponse(
    val appearanceId: Int,
    val breed: String,
    val hairColor: String,
    val weight: Int,
    val hairLength: String,
)