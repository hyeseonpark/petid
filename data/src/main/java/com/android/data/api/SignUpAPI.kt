package com.android.data.api

import android.telecom.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpAPI {
    @POST("/auth/oauth2/login")
    suspend fun Login(@Body login: login): Call<LoginResponse>
}