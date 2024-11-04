package com.android.data.dto.response

import com.android.domain.entity.ContentEntity
import com.android.domain.entity.ContentLikeEntity

data class ContentLikeResponse(
    val contentId: Int,
    val likeCount: Int,
)

fun ContentLikeResponse.toDomain() = ContentLikeEntity(
    contentId = contentId,
    likeCount = likeCount
)