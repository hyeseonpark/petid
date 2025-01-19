package com.petid.domain.usecase

import com.petid.domain.entity.CommonInfo
import com.petid.domain.repository.BlogMainRepository
import kotlinx.coroutines.flow.Flow

class GetCommonInfoListUseCase(
    private val blogMainRepository: BlogMainRepository,
) {
//    operator fun invoke(query: String): Flow<List<CommonInfo>> {
//        blogMainRepository.getContentList(query)
//    }
}