package com.petid.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentEntity(
    val contentId: Int,
    val title: String,
    val body: String,
    val category: String,
    val imageUrl: String?, // TODO 백엔드 수정 시 nullable 삭제
    val createdAt: String,
    val updatedAt: String,
    var likesCount: Int,
    val authorId: Int,
    var isLiked: Boolean
) : Parcelable