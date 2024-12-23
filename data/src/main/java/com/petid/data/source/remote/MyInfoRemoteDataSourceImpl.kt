package com.petid.data.source.remote

import com.petid.data.api.MemberAPI
import com.petid.data.dto.request.UpdateMemberInfoRequest
import com.petid.data.dto.response.ErrorResponse
import com.petid.data.dto.response.toDomain
import com.petid.domain.entity.MemberInfoEntity
import com.petid.domain.entity.UpdateMemberInfoEntity
import com.petid.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class MyInfoRemoteDataSourceImpl @Inject constructor(
    private val memberAPI: MemberAPI,
): MyInfoRemoteDataSource {
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

    override suspend fun getProfileImageUrl(imagePath: String): ApiResult<String> {
        return try {
            val response = memberAPI.getProfileImageUrl(imagePath)
            ApiResult.Success(response)
        } catch (e: HttpException) {
            val gson = Gson()
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)

            ApiResult.HttpError(errorResponse.toDomain())

        } catch (e: Exception) {
            ApiResult.Error(e.message)
        }
    }

    override suspend fun updateMemberInfo(
        address: String, addressDetail: String, phone: String
    ): ApiResult<UpdateMemberInfoEntity> {
        return try {
            val response = memberAPI.updateMemberInfo(
                UpdateMemberInfoRequest(address, addressDetail, phone)
            )
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

    override suspend fun updateMemberPhoto(filePath: String): ApiResult<String> {
        return try {
            val response = memberAPI.updateMemberPhoto(filePath)
            ApiResult.Success(response)
        } catch (e: HttpException) {
            val gson = Gson()
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)

            ApiResult.HttpError(errorResponse.toDomain())

        } catch (e: Exception) {
            ApiResult.Error(e.message)
        }
    }

    override suspend fun doWithdraw(): ApiResult<Unit> {
        return try {
            val response = memberAPI.doWithdraw()
            ApiResult.Success(response)
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