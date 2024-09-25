package com.android.domain.usecase.main

import com.android.domain.entity.BannerEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.repository.HomeMainRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject

class GetBannerListUseCase @Inject constructor(
    private val homeMainRepository: HomeMainRepository,
){
    suspend operator fun invoke(type: String): ApiResult<List<BannerEntity>> {
        return homeMainRepository.getBannerList(type)
    }
}