package com.android.domain.usecase.hospital

import com.android.domain.entity.HospitalOrderEntity
import com.android.domain.repository.ReservationCalendarRepository
import com.android.domain.util.ApiResult
import java.util.Date
import javax.inject.Inject

class CreateHospitalOrderUseCase @Inject constructor(
    private val reservationCalendarRepository: ReservationCalendarRepository
){
    suspend operator fun invoke(hospitalId: Int, date: String): ApiResult<HospitalOrderEntity> {
        return reservationCalendarRepository.createHospitalOrder(hospitalId, date)
    }
}