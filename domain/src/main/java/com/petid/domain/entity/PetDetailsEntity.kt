package com.petid.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PetDetailsEntity(
    val petId: Int,
    val ownerId: String,
    val petRegNo: String?,
    val petName: String,
    val petBirthDate: String,
    val petSex: String,
    val petNeuteredYn: String,
    val petNeuteredDate: String?,
    val appearance: PetAppearanceEntity,
    val chipType: String,
    var petImages: List<PetImageEntity>,
) : Parcelable