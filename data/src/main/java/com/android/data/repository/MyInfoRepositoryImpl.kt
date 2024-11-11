package com.android.data.repository

import com.android.data.api.MemberAPI
import com.android.data.dto.response.ErrorResponse
import com.android.data.dto.response.toDomain
import com.android.domain.entity.MemberInfoEntity
import com.android.domain.entity.UpdateMemberInfoEntity
import com.android.domain.repository.MyInfoRepository
import com.android.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyInfoRepositoryImpl @Inject constructor(
    private val memberAPI: MemberAPI,
): MyInfoRepository {
    override suspend fun getMemberInfo(): ApiResult<MemberInfoEntity> {
        return try {
            val response = memberAPI.getMemberInfo()
            ApiResult.Success(response.toDomain())
        } catch (e: HttpException) {
            val gson = Gson()
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)

            ApiResult.HttpError(errorResponse.toDomain())

        } catch (e: Exception) {
            ApiResult.Error(e.message)
        }
    }

    override suspend fun uploadProfileImage(imagePath: String): String {
        return try {
            memberAPI.uploadProfileImage(imagePath)
        } catch (e: Exception) {
            ""
        }
    }

    override suspend fun getProfileImageUrl(imagePath: String): String {
        return try {
            memberAPI.getProfileImageUrl(imagePath)
        } catch (e: Exception) {
            ""
        }
    }

    override suspend fun updateMemberInfo(): ApiResult<UpdateMemberInfoEntity> {
        return try {
            val response = memberAPI.updateMemberInfo("", "","","")
            ApiResult.Success(response.toDomain())
        } catch (e: HttpException) {
            val gson = Gson()
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)

            ApiResult.HttpError(errorResponse.toDomain())

        } catch (e: Exception) {
            ApiResult.Error(e.message)
        }
    }
}