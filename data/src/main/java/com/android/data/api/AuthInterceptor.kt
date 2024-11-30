package com.android.data.api

import com.amazonaws.http.HttpHeader.AUTHORIZATION
import com.android.data.util.Constants.SHARED_VALUE_ACCESS_TOKEN
import com.android.data.util.PreferencesHelper
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.HttpURLConnection.HTTP_OK
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

class AuthInterceptor(
    private val preferencesHelper: PreferencesHelper
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String =
            preferencesHelper.getStringValue(SHARED_VALUE_ACCESS_TOKEN)
         ?: return errorResponse(chain.request())

        val request = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, "Bearer $token").build()

        val response = chain.proceed(request)
        if (response.code == HTTP_OK) {
            val newAccessToken: String = response.header(AUTHORIZATION, null) ?: return response
            Logger.d("new Access Token = $newAccessToken")

            CoroutineScope(Dispatchers.IO).launch {
                val existedAccessToken = preferencesHelper.getStringValue(SHARED_VALUE_ACCESS_TOKEN)
                if (existedAccessToken != newAccessToken) {
                    preferencesHelper.saveStringValue(SHARED_VALUE_ACCESS_TOKEN, newAccessToken)
                    Logger.d("newAccessToken = ${newAccessToken}\nExistedAccessToken = $existedAccessToken")
                }
            }
        } else {
            Logger.e("${response.code} : ${response.request} \n ${response.message}")
        }
        return response
    }

    private fun errorResponse(request: Request): Response = Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_2)
        .code(HTTP_UNAUTHORIZED)
        .message("")
        .body("".toResponseBody(null))
        .build()
}