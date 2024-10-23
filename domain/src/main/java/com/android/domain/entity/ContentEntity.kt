package com.android.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContentEntity(
    val contentId: Int,
    val title: String,
    val body: String,
    val category: String,
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String,
    var likesCount: Int,
    val authorId: Int,
    var isLiked: Boolean
) : Parcelable