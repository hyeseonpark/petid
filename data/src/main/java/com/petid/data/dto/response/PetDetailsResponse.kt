package com.petid.data.dto.response

import com.petid.domain.entity.PetDetailsEntity

data class PetDetailsResponse(
    val petId: Int,
    val ownerId: String,
    val petRegNo: String?,
    val petName: String,
    val petBirthDate: String,
    val petSex: String,
    val petNeuteredYn: String,
    val petNeuteredDate: String?,
    val chipType: String,
    val appearance: PetAppearanceResponse,
    val petImages: List<PetImageResponse>,
)

fun PetDetailsResponse.toDomain() = PetDetailsEntity(
    petId = petId,
    ownerId = ownerId,
    petRegNo = petRegNo,
    petName = petName,
    petBirthDate = petBirthDate,
    petSex = petSex,
    petNeuteredYn = petNeuteredYn,
    petNeuteredDate = petNeuteredDate,
    chipType = chipType,
    appearance = appearance.toDomain(),
    petImages = petImages.toDomain(),
)