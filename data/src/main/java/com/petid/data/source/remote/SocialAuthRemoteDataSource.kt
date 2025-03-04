package com.petid.data.source.remote

import com.petid.data.dto.response.AuthResponse

interface SocialAuthRemoteDataSource {
        suspend fun getLogin(sub: String, fcmToken: String): AuthResponse
        suspend fun doRestore(): Unit
}