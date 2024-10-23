package com.android.data.source.remote

import com.android.data.api.ContentAPI
import com.android.data.dto.response.ErrorResponse
import com.android.data.dto.response.toDomain
import com.android.domain.entity.ContentEntity
import com.android.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentDetailRemoteRemoteDataSourceImpl @Inject constructor(
    private val contentAPI: ContentAPI
): ContentDetailRemoteDataSource {
    override suspend fun getContentDetail(contentId: Int): ApiResult<ContentEntity> {
        return try {
            val response = contentAPI.getContentDetail(contentId)
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