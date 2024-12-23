package com.petid.data.api

import com.petid.data.dto.response.AuthResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthAPI {

    /**
     * 로그인 및 토큰 발급
     */
    @FormUrlEncoded
    @POST("auth/oauth2/login")
    suspend fun login(
        @Field("sub") sub: String,
        @Field("fcmToken") fcmToken: String
    ): AuthResponse

    /**
     * 회원가입
     */
    @FormUrlEncoded
    @POST("auth/oauth2/join/{platform}")
    suspend fun join(
        @Path("platform") platform: String,
        @Field("token") token: String,
        @Field("fcmToken") fcmToken: String,
        @Field("ad") ad: Boolean
    ): AuthResponse

    /**
     * 토큰 재발급
     */
    @FormUrlEncoded
    @POST("auth/token/refresh")
    suspend fun refresh(
        @Field("refreshToken") refreshToken : String
    ) : AuthResponse
}