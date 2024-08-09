package com.android.data.model

import com.android.domain.entity.LoginEntity
import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("accesToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)

fun Login.toDomain() = LoginEntity(
    accessToken = accessToken,
    refreshToken = refreshToken
)