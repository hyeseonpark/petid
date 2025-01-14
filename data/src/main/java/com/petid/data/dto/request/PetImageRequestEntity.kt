package com.petid.data.dto.request

import com.petid.domain.entity.PetImage

data class PetImageRequest(
    val imagePath: String,
)

fun PetImage.toDto() = PetImageRequest(
    imagePath = imagePath
)

fun List<PetImage>.toDto(): List<PetImageRequest> {
    return this.map { it.toDto() }
}
