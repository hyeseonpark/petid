package com.petid.data.repository

import com.petid.data.source.remote.ContentDetailRemoteDataSource
import com.petid.domain.entity.ContentEntity
import com.petid.domain.repository.ContentDetailRepository
import com.petid.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentDetailRepositoryImpl @Inject constructor(
    private val remoteDataSource: ContentDetailRemoteDataSource,
): ContentDetailRepository {
    override suspend fun getContentDetail(contentId: Int): ApiResult<ContentEntity> {
        return when (val result = remoteDataSource.getContentDetail(contentId)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }
}