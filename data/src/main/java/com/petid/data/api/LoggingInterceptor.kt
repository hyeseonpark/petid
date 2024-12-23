package com.petid.data.api

import okhttp3.Interceptor
import okhttp3.Response
import com.orhanobut.logger.Logger
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggingInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Request 정보 로그 출력
        Logger.d("Sending request to URL: ${request.url}${"\n"}Request Headers: ${request.headers}")

        // Request Body 로그 출력
        request.body?.let { requestBody ->
            val buffer = Buffer()
            requestBody.writeTo(buffer)

            // UTF-8로 변환하여 로그에 출력
            val charset = StandardCharsets.UTF_8
            Logger.d("Request Body: ${buffer.readString(charset)}")
        } ?: Logger.d("No Request Body")

        val response = chain.proceed(request)

        // Response 정보 로그 출력
        Logger.d("Received response from URL: ${response.request.url}")
        Logger.d("Response Headers: ${response.headers}")
        val responseBody = response.peekBody(Long.MAX_VALUE)
        Logger.json(responseBody.string())

        return response
    }
}