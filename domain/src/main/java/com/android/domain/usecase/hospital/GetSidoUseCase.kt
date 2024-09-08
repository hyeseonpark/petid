package com.android.domain.usecase.hospital

import com.android.domain.entity.AuthEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.repository.HospitalMainRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject

class GetSidoUseCase @Inject constructor(
    private val hospitalMainRepository: HospitalMainRepository
){
    suspend operator fun invoke(): ApiResult<List<LocationEntity>> {
        return hospitalMainRepository.getSido()
    }
}