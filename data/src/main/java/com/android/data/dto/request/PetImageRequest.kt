package com.android.data.dto.request

import com.android.domain.entity.PetImageRequestEntity

data class PetImageRequest(
    val imagePath: String,
)

fun PetImageRequest.toDomain() = PetImageRequestEntity(
    imagePath = imagePath
)

fun List<PetImageRequest>.toDomain(): List<PetImageRequestEntity> {
    return this.map { it.toDomain() }
}
