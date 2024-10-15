package com.android.domain.usecase.hospital

import com.android.domain.entity.HospitalOrderDetailEntity
import com.android.domain.repository.ReservationHistoryInfoRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject

class ReservationHistoryInfoUseCase @Inject constructor(
    private val reservationHistoryInfoRepository: ReservationHistoryInfoRepository
){
    suspend operator fun invoke(status: String): ApiResult<List<HospitalOrderDetailEntity>> {
        return reservationHistoryInfoRepository.getHospitalReservationHistoryList(status)
    }
}