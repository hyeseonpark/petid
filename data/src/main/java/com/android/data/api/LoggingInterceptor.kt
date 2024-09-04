package com.android.data.api

import okhttp3.Interceptor
import okhttp3.Response
import com.orhanobut.logger.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggingInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Request 정보 로그 출력
        Logger.d("Sending request to URL: ${request.url}${"\n"}Request Headers: ${request.headers}")
        request.body?.let {
            Logger.d("Request Body: ${it}")
        }

        val response = chain.proceed(request)

        // Response 정보 로그 출력
        Logger.d("Received response from URL: ${response.request.url}")
        Logger.d("Response Headers: ${response.headers}")
        val responseBody = response.peekBody(Long.MAX_VALUE)
        Logger.json(responseBody.string())

        return response
    }
}