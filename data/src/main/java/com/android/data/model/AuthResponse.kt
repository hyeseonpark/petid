package com.android.data.model

import com.android.domain.entity.AuthEntity
import com.google.gson.annotations.SerializedName
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