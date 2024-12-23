package com.petid.domain.entity

data class PetRequestEntity(
    val petName: String,
    val petBirthDate: String,
    val petSex: Char,
    val petNeuteredYn: Char,
    val petNeuteredDate: String?,
    val chipType: String,
    val appearance: PetAppearanceRequestEntity,
    val petImages: List<PetImageRequestEntity>,
    val proposer: PetProposerRequestEntity,
    val sign: String,
)