package com.android.domain.entity

data class ErrorEntity(
    val timestamp: Long,
    val status: Int,
    val error: String,
    val path: String
)