package com.android.data.api

import com.android.data.util.Constants.SHARED_VALUE_ACCESS_TOKEN
import com.android.data.util.PreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val preferencesHelper: PreferencesHelper
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requeset = chain.request().newBuilder()
            .addHeader(
                "Authorization",
                preferencesHelper.getStringValue(SHARED_VALUE_ACCESS_TOKEN) ?: "")
            .build()

        return chain.proceed(requeset)
    }
}