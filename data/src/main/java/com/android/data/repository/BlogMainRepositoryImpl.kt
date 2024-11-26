package com.android.data.repository

import com.android.data.api.ContentAPI
import com.android.data.dto.request.UpdateMemberInfoRequest
import com.android.data.dto.response.ErrorResponse
import com.android.data.dto.response.toDomain
import com.android.data.source.remote.BlogMainRemoteDataSource
import com.android.domain.entity.ContentEntity
import com.android.domain.entity.ContentLikeEntity
import com.android.domain.repository.BlogMainRepository
import com.android.domain.util.ApiResult
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlogMainRepositoryImpl @Inject constructor(
    private val remoteDataSource: BlogMainRemoteDataSource,
    private val contentAPI: ContentAPI,
): BlogMainRepository {
    override suspend fun getContentList(category: String): ApiResult<List<ContentEntity>> {
        return when (val result = remoteDataSource.getContentList(category)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun getContentImage(filePath: String): String {
        return contentAPI.getContentImage(filePath)
    }

    override suspend fun doContentLike(contentId: Int): ApiResult<ContentLikeEntity> {
        return when (val result = remoteDataSource.doContentLike(contentId)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun cancelContentLike(contentId: Int): ApiResult<ContentLikeEntity> {
        return try {
            val response = contentAPI.cancelContentLike(contentId)
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