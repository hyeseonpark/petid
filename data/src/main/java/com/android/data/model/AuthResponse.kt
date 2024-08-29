package com.android.data.model

import com.android.domain.entity.AuthEntity
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String
)

fun AuthResponse.toDomain() = AuthEntity(
    accessToken = accessToken,
    refreshToken = refreshToken
)