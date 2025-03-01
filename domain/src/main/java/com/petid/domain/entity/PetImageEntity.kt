package com.petid.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PetImageEntity (
    val petImageId: Int,
    val petId: Int,
    val imagePath: String,
) : Parcelable