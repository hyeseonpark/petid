package com.android.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PetDetailsEntity(
    val petId: Int,
    val ownerId: String,
    val petRegNo: Int,
    val petName: String,
    val petBirthDate: String,
    val petSex: String,
    val petNeuteredYn: String,
    val petNeuteredDate: String,
    val petAddr: String,
    val appearance: PetAppearanceEntity,
    val petImages: List<PetImageEntity>,
) : Parcelable