package com.petid.domain.usecase

import com.petid.domain.repository.BlogMainRepository

class GetCommonInfoListUseCase(
    private val blogMainRepository: BlogMainRepository,
) {
//    operator fun invoke(query: String): Flow<List<CommonInfo>> {
//        blogMainRepository.getContentList(query)
//    }
}