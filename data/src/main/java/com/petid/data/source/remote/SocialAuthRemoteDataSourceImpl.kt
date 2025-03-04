package com.petid.data.source.remote

import com.petid.data.api.AuthAPI
import com.petid.data.api.MemberAPI
import com.petid.data.dto.response.AuthResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialAuthRemoteDataSourceImpl @Inject constructor(
    private val authAPI: AuthAPI,
    private val memberAPI: MemberAPI
) : SocialAuthRemoteDataSource {

    override suspend fun getLogin(sub: String, fcmToken: String): AuthResponse =
        authAPI.login(sub, fcmToken)

    override suspend fun doRestore(): Unit =
        memberAPI.doRestore()
}
