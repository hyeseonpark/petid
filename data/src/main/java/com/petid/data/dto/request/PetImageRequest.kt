package com.petid.data.dto.request

import com.petid.domain.entity.PetImageRequestEntity

data class PetImageRequest(
    val imagePath: String,
)

fun PetImageRequest.toDomain() = PetImageRequestEntity(
    imagePath = imagePath
)

fun List<PetImageRequest>.toDomain(): List<PetImageRequestEntity> {
    return this.map { it.toDomain() }
}
