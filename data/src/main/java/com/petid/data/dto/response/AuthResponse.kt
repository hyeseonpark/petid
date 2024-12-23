package com.petid.data.dto.response

import com.petid.domain.entity.AuthEntity
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