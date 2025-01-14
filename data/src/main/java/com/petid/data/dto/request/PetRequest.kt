package com.petid.data.dto.request

import com.petid.domain.entity.Pet

data class PetRequest(
    val petName: String?,
    val petBirthDate: String?,
    val petSex: Char?,
    val petNeuteredYn: Char?,
    val petNeuteredDate: String?,
    val chipType: String?,
    val appearance: PetAppearanceRequest?,
    val petImages: List<PetImageRequest>?,
    val proposer: PetProposerRequest?,
    val sign: String?,
)

fun Pet.toDto() = PetRequest(
    petName = petName,
    petBirthDate = petBirthDate,
    petSex = petSex,
    petNeuteredYn = petNeuteredYn,
    petNeuteredDate = petNeuteredDate,
    chipType = chipType,
    appearance = appearance.toDto(),
    petImages = petImages.toDto(),
    proposer = proposer.toDto(),
    sign = sign,
)
