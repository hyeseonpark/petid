package com.petid.data.source.remote

import com.petid.data.api.ContentAPI
import com.petid.data.dto.response.ContentResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentDetailRemoteRemoteDataSourceImpl @Inject constructor(
    private val contentAPI: ContentAPI
): ContentDetailRemoteDataSource {
    override suspend fun getContentDetail(contentId: Int): ContentResponse =
        contentAPI.getContentDetail(contentId)
}