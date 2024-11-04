package com.android.domain.usecase.main

import com.android.domain.entity.BannerEntity
import com.android.domain.repository.HomeMainRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject

class GetBannerImageUseCase @Inject constructor(
    private val homeMainRepository: HomeMainRepository,
){
//    suspend operator fun invoke(imagePath: String): ApiResult<String> {
//        return homeMainRepository.getBannerImage(imagePath)
//    }
    suspend operator fun invoke(imagePath: String): String {
        return homeMainRepository.getBannerImage(imagePath)
    }
}