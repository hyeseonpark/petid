package com.android.data.dto.response

import com.android.domain.entity.PetImageEntity

data class PetImageResponse(
    val petImageId: Int,
    val petId: Int,
    val imagePath: Int,
)

fun PetImageResponse.toDomain() = PetImageEntity(
    petImageId = petImageId,
    petId = petId,
    imagePath = imagePath,
)

fun List<PetImageResponse>.toDomain(): List<PetImageEntity> {
    return this.map { it.toDomain() }
}