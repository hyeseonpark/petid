package com.android.domain.usecase.hospital

import com.android.domain.entity.HospitalEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.repository.HospitalMainRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject

class GetHospitalListUseCase @Inject constructor(
    private val hospitalMainRepository: HospitalMainRepository
){
    suspend operator fun invoke(sidoId: Int, sigunguId: Int, eupmundingId: Int): ApiResult<List<HospitalEntity>> {
        return hospitalMainRepository.getHospitalList(sidoId, sigunguId, eupmundingId)
    }
}