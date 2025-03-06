package com.petid.data.repository.remote

import com.petid.data.api.ContentAPI
import com.petid.data.dto.response.toDomain
import com.petid.data.dto.response.toCommonInfoDomain
import com.petid.data.source.remote.BlogMainRemoteDataSource
import com.petid.data.util.mapApiResult
import com.petid.domain.entity.CommonInfo
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
        runCatching {
            remoteDataSource.getContentList(category).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getCommonInfoList(category: String): ApiResult<List<CommonInfo>>  =
        runCatching {
            remoteDataSource.getContentList(category).toCommonInfoDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun doContentLike(contentId: Int): ApiResult<ContentLikeEntity> =
        runCatching {
            remoteDataSource.doContentLike(contentId).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun cancelContentLike(contentId: Int): ApiResult<ContentLikeEntity> =
        runCatching {
            remoteDataSource.cancelContentLike(contentId).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    // TODO 변경 필
    override suspend fun getContentImage(filePath: String): String =
        contentAPI.getContentImage(filePath)
}