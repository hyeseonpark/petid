package com.petid.data.repository.remote

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
    override suspend fun getContentList(category: String): ApiResult<List<ContentEntity>> =
        remoteDataSource.getContentList(category)

    // TODO 변경 필
    override suspend fun getContentImage(filePath: String): String =
        contentAPI.getContentImage(filePath)

    override suspend fun doContentLike(contentId: Int): ApiResult<ContentLikeEntity> =
        remoteDataSource.doContentLike(contentId)

    override suspend fun cancelContentLike(contentId: Int): ApiResult<ContentLikeEntity> =
        remoteDataSource.cancelContentLike(contentId)
}