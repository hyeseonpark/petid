package com.android.data.repository

import com.android.data.api.ContentAPI
import com.android.data.source.remote.BlogMainRemoteDataSource
import com.android.domain.entity.ContentEntity
import com.android.domain.entity.ContentLikeEntity
import com.android.domain.repository.BlogMainRepository
import com.android.domain.util.ApiResult
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
}