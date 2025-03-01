package com.petid.domain.entity

data class BannerEntity(
    val id: Int,
    var imageUrl: String,
    val text: String,
    val type: String,
    val status: String,
    val contentId: Int?,
)