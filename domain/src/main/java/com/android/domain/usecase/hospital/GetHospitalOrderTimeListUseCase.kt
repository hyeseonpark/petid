package com.android.domain.usecase.hospital

import com.android.domain.repository.HospitalMainRepository
import com.android.domain.repository.ReservationCalendarRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject

class GetHospitalOrderTimeListUseCase @Inject constructor(
    private val reservationCalendarRepository: ReservationCalendarRepository
) {
    suspend operator fun invoke(hospitalId: Int, day: String, date: String): ApiResult<List<String>> {
        return reservationCalendarRepository.getHospitalOrderTimeList(hospitalId, day, date)
    }
}