package com.android.data.repository.login

import com.android.data.api.SignUpAPI
import com.android.data.model.toDomain
import com.android.domain.entity.LoginEntity
import com.android.domain.util.ApiResult

class LoginRemoteDataSource(private val signUpAPI: SignUpAPI) : LoginDataSource.Remote {
    override suspend fun getLogin(sub: String, fcmToken: String): ApiResult<LoginEntity> = try {
        val result = signUpAPI.Login(sub, fcmToken)
        // 성공 시, DTO에서 Domain 모델로 변환
        ApiResult.Success(result.toDomain())
    } catch (e: Exception) {
        ApiResult.Error(e)
    }

}