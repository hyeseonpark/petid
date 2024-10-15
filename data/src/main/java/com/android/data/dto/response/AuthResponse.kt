package com.android.data.dto.response

import com.android.domain.entity.AuthEntity
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)

fun AuthResponse.toDomain() = AuthEntity(
    accessToken = accessToken,
    refreshToken = refreshToken
)