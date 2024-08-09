package com.android.data.api

import com.android.data.model.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.Call

interface SignUpAPI {
    @FormUrlEncoded
    @POST("/auth/oauth2/login")
    suspend fun Login(
        @Field("sub") sub: String,
        @Field("fcmToken") fcmToken: String
    ): LoginResponse
}