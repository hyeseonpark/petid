package com.android.data.dto.response

import com.android.domain.entity.PetDetailsEntity

data class PetDetailsResponse(
    val petId: Int,
    val ownerId: String,
    val petRegNo: Int,
    val petName: String,
    val petBirthDate: String,
    val petSex: String,
    val petNeuteredYn: String,
    val petNeuteredDate: String,
    val petAddr: String,
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
    petAddr = petAddr,
    chipType = chipType,
    appearance = appearance.toDomain(),
    petImages = petImages.toDomain(),
)