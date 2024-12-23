package com.petid.domain.entity

data class PetUpdateEntity(
    val petNeuteredDate: String?,
    val appearance: UpdateAppearanceEntity,
)