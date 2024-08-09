package com.android.data.model

import com.android.domain.entity.LoginEntity
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accesToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)

fun LoginResponse.toDomain() = LoginEntity(
    accessToken = accessToken,
    refreshToken = refreshToken
)