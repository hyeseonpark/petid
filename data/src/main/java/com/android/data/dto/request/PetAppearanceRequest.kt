package com.android.data.dto.request

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