package com.petid.data.dto.response

import com.petid.domain.entity.ContentEntity
import com.petid.domain.entity.ContentLikeEntity

data class ContentLikeResponse(
    val contentId: Int,
    val likeCount: Int,
)

fun ContentLikeResponse.toDomain() = ContentLikeEntity(
    contentId = contentId,
    likeCount = likeCount
)