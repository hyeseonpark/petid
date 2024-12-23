package com.petid.data.repository

import com.petid.data.api.ContentAPI
import com.petid.data.source.remote.BlogMainRemoteDataSource
import com.petid.domain.entity.ContentEntity
import com.petid.domain.entity.ContentLikeEntity
import com.petid.domain.repository.BlogMainRepository
import com.petid.domain.util.ApiResult
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
        return when (val result = remoteDataSource.cancelContentLike(contentId)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }
}