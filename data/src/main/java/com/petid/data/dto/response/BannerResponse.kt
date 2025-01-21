package com.petid.data.dto.response

import com.petid.domain.entity.BannerEntity
import kotlinx.serialization.Serializable

@Serializable
data class BannerResponse(
    val id: Int,
    val imageUrl: String,
    val text: String,
    val type: String,
    val status: String,
    val contentId: Int?,
)

fun BannerResponse.toDomain() = BannerEntity(
    id = id,
    imageUrl = imageUrl,
    text = text,
    type = type,
    status = status,
    contentId = contentId,
)

fun List<BannerResponse>.toDomain(): List<BannerEntity> {
    return this.map { it.toDomain() }
}
