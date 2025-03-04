package com.petid.data.source.remote

import com.petid.data.api.AuthAPI
import com.petid.data.dto.response.AuthResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermsRemoteDataSourceImpl @Inject constructor(
    private val authAPI: AuthAPI
) : TermsRemoteDataSource {
    override suspend fun doJoin(platform: String, token: String, fcmToken: String, ad: Boolean
    ): AuthResponse =
        authAPI.join(platform, token, fcmToken, ad)
}