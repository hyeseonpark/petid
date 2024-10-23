package com.android.domain.usecase.content

import com.android.domain.entity.ContentEntity
import com.android.domain.repository.BlogMainRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject

class DoContentLikeUseCase @Inject constructor(
    private val blogMainRepository: BlogMainRepository
){
    suspend operator fun invoke(category: Int): ApiResult<Unit> {
        return blogMainRepository.doContentLike(category)
    }
}