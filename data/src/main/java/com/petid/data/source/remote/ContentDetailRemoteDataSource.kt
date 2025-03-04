package com.petid.data.source.remote

import com.petid.data.dto.response.ContentResponse

interface ContentDetailRemoteDataSource {
    suspend fun getContentDetail(contentId: Int): ContentResponse
}