package com.petid.data.dto.request

import com.petid.domain.entity.PetUpdateEntity

data class PetUpdateRequest(
    val petNeuteredDate: String?,
    val appearance: UpdateAppearanceRequest,
)

fun PetUpdateEntity.toDto() =
    PetUpdateRequest(
        petNeuteredDate = petNeuteredDate,
        appearance = appearance.toDto()
    )