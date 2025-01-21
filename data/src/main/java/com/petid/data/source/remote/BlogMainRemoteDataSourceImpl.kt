package com.petid.data.source.remote

import com.petid.data.api.ContentAPI
import com.petid.data.dto.response.toCommonInfoDomain
import com.petid.data.dto.response.toDomain
import com.petid.data.util.mapApiResult
import com.petid.domain.entity.CommonInfo
import com.petid.domain.entity.ContentEntity
import com.petid.domain.entity.ContentLikeEntity
import com.petid.domain.util.ApiResult
import javax.inject.Inject

class BlogMainRemoteDataSourceImpl @Inject constructor(
    private val contentAPI: ContentAPI
): BlogMainRemoteDataSource {
    override suspend fun getContentList(category: String): ApiResult<List<ContentEntity>> =
        runCatching {
            contentAPI.getContentList(category).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun getCommonInfoList(category: String): ApiResult<List<CommonInfo>> =
        runCatching {
            contentAPI.getContentList(category).toCommonInfoDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun doContentLike(contentId: Int): ApiResult<ContentLikeEntity> =
        runCatching {
            contentAPI.doContentLike(contentId).toDomain()
        }.mapApiResult { ApiResult.Success(it) }

    override suspend fun cancelContentLike(contentId: Int): ApiResult<ContentLikeEntity> =
        runCatching {
            contentAPI.cancelContentLike(contentId).toDomain()
        }.mapApiResult { ApiResult.Success(it) }
}