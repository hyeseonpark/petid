package com.petid.data.dto.response

import com.petid.domain.entity.PetImageEntity

data class PetImageResponse(
    val petImageId: Int,
    val petId: Int,
    val imagePath: String,
)

fun PetImageResponse.toDomain() = PetImageEntity(
    petImageId = petImageId,
    petId = petId,
    imagePath = imagePath,
)

fun List<PetImageResponse>.toDomain(): List<PetImageEntity> {
    return this.map { it.toDomain() }
}