package com.petid.data.dto.request

import com.petid.domain.entity.PetAppearance

/**
 *
 * @param breed 견종
 * @param hairColor 털 색깔
 * @param weight 몸무게
 * @param hairLength 털 길이
 */
data class PetAppearanceRequest (
    val breed: String,
    val hairColor: String,
    val weight: Int,
    val hairLength: String,
)

fun PetAppearance.toDto() = PetAppearanceRequest(
    breed = breed,
    hairColor = hairColor,
    weight = weight,
    hairLength = hairLength
)