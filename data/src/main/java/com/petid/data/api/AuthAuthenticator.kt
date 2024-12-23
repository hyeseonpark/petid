package com.petid.data.api

import android.util.Log
import com.amazonaws.http.HttpHeader.AUTHORIZATION
import com.petid.data.BuildConfig
import com.petid.data.util.Constants.SHARED_VALUE_ACCESS_TOKEN
import com.petid.data.util.Constants.SHARED_VALUE_REFRESH_TOKEN
import com.petid.data.util.PreferencesHelper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class AuthAuthenticator(
    private val preferencesHelper: PreferencesHelper
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // 기존 토큰 가져오기
        val currentToken = preferencesHelper.getStringValue(SHARED_VALUE_REFRESH_TOKEN)

        // 토큰이 이미 실패한 경우 null 반환 (무한 루프 방지)
        if (response.request.header(AUTHORIZATION)?.contains(currentToken.toString()) == true) {
            return null
        }

        // SHARED_VALUE_REFRESH_TOKEN이 비어있으면 null 반환
        if (currentToken.isNullOrBlank()) {
            response.close()
            return null
        }

        return runBlocking {
            try {
                val newAccessToken = RefreshTokenService.refreshToken(currentToken)

                preferencesHelper.saveStringValue(SHARED_VALUE_ACCESS_TOKEN, newAccessToken)
                newRequestWithToken(newAccessToken, response.request)
            } catch (e: Exception) {
                Log.e("AuthAuthenticator", "토큰 갱신 실패: ${e.message}", e)
                null
            }
        }
    }

    private fun newRequestWithToken(token: String, request: Request): Request =
        request.newBuilder()
            .header(AUTHORIZATION, token)
            .build()

}

object RefreshTokenService{
    private val authAPIService: AuthAPI = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor())
                .build()
        )
        .addConverterFactory(NullOnEmptyConverterFactory())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AuthAPI::class.java)

    suspend fun refreshToken(refreshToken: String): String {
        return flow {
            val response = authAPIService.refresh(refreshToken)
            val newAccessToken = response.accessToken.split(" ").last()
            emit(newAccessToken)
        }.catch { e ->
            throw IllegalStateException("토큰 재발급 실패: $e")
        }.first()
    }
}