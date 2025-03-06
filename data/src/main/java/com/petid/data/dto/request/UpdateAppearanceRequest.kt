package com.petid.data.dto.request

import com.petid.domain.entity.UpdateAppearanceEntity

data class UpdateAppearanceRequest(
    val weight: Int?,
)

fun UpdateAppearanceEntity.toDto() =
    UpdateAppearanceRequest(
        weight = weight
    )
