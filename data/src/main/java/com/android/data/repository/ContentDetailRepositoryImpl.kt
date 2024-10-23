package com.android.data.repository

import com.android.data.source.remote.ContentDetailRemoteDataSource
import com.android.domain.entity.ContentEntity
import com.android.domain.repository.ContentDetailRepository
import com.android.domain.util.ApiResult
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