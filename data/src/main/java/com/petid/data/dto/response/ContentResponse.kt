package com.petid.data.dto.response

import com.petid.domain.entity.CommonInfo
import com.petid.domain.entity.ContentEntity

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
) {
    fun toDomain() = ContentEntity(
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
    fun toCommonInfoDomain() = CommonInfo(
        contentId = contentId,
        title = title,
        body = body,
    )
}

fun List<ContentResponse>.toDomain(): List<ContentEntity> {
    return this.map { it.toDomain() }
}

fun List<ContentResponse>.toCommonInfoDomain(): List<CommonInfo> {
    return this.map { it.toCommonInfoDomain() }
}