package com.petid.data.source.remote

import com.petid.data.dto.response.AuthResponse

interface TermsRemoteDataSource {
    suspend fun doJoin(platform: String, token: String, fcmToken: String, ad: Boolean): AuthResponse
}