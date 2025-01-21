package com.petid.data.dto.response

import com.petid.domain.entity.PetAppearanceEntity

data class PetAppearanceResponse(
    val appearanceId: Int,
    val breed: String,
    val hairColor: String,
    val weight: Double,
    val hairLength: String,
)
fun PetAppearanceResponse.toDomain() = PetAppearanceEntity(
    appearanceId = appearanceId,
    breed = breed,
    hairColor = hairColor,
    weight = weight,
    hairLength = hairLength,
)