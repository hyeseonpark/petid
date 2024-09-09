package com.android.domain.usecase.hospital

import com.android.domain.entity.LocationEntity
import com.android.domain.repository.HospitalMainRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject

class GetEupmundongUseCase @Inject constructor(
    private val hospitalMainRepository: HospitalMainRepository
){
    suspend operator fun invoke(id: Int): ApiResult<List<LocationEntity>> {
        return hospitalMainRepository.getEupmundongList(id)
    }
}