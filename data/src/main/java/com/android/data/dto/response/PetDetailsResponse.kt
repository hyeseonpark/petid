package com.android.data.dto.response

data class PetDetailsResponse(
    val petId: Int,
    val ownerId: String,
    val petRegNo: Int,
    val petName: String,
    val petSex: String,
    val petNeuteredYn: String,
    val petNeuteredDate: String,
    val petAddr: String,
    val appearance: PetAppearanceResponse,
    val petImages: List<PetImageResponse>,
)