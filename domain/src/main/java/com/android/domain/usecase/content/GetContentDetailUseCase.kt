package com.android.domain.usecase.content

import com.android.domain.entity.ContentEntity
import com.android.domain.repository.ContentDetailRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject

class GetContentDetailUseCase @Inject constructor(
    private val contentDetailRepository: ContentDetailRepository
){
    suspend operator fun invoke(contentId: Int): ApiResult<ContentEntity> {
        return contentDetailRepository.getContentDetail(contentId)
    }
}