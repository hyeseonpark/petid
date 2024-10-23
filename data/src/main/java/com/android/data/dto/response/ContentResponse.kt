package com.android.data.dto.response

import com.android.domain.entity.ContentEntity
import com.android.domain.entity.LocationEntity

data class ContentResponse(
    val contentId: Int,
    val title: String,
    val body: String,
    val category: String,
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val likesCount: Int,
    val authorId: Int,
    val isLiked: Boolean
)
fun ContentResponse.toDomain() = ContentEntity(
    contentId = contentId,
    title = title,
    body = body,
    category = category,
    imageUrl = imageUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    likesCount = likesCount,
    authorId = authorId,
    isLiked = isLiked
)

fun List<ContentResponse>.toDomain(): List<ContentEntity> {
    return this.map { it.toDomain() }
}