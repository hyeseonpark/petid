package com.petid.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PetAppearanceEntity (
    val appearanceId: Int,
    val breed: String,
    val hairColor: String,
    val weight: Double,
    val hairLength: String,
) : Parcelable