package com.android.data.dto.response

import com.android.domain.entity.PetAppearanceEntity

data class PetAppearanceResponse(
    val appearanceId: Int,
    val breed: String,
    val hairColor: String,
    val weight: Int,
    val hairLength: String,
)
fun PetAppearanceResponse.toDomain() = PetAppearanceEntity(
    appearanceId = appearanceId,
    breed = breed,
    hairColor = hairColor,
    weight = weight,
    hairLength = hairLength,
)