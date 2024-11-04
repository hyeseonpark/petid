package com.android.data.source.remote

import com.android.data.api.ContentAPI
import com.android.data.dto.response.ErrorResponse
import com.android.data.dto.response.toDomain
import com.android.domain.entity.ContentEntity
import com.android.domain.entity.ContentLikeEntity
import com.android.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class BlogMainRemoteDataSourceImpl @Inject constructor(
    private val contentAPI: ContentAPI
): BlogMainRemoteDataSource {
    override suspend fun getContentList(category: String): ApiResult<List<ContentEntity>> {
        return try {
            val response = contentAPI.getContentList(category)
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

    override suspend fun doContentLike(contentId: Int): ApiResult<ContentLikeEntity> {
        return try {
            val response = contentAPI.doContentLike(contentId)
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