package com.petid.domain.usecase

import com.petid.domain.repository.HospitalMainRepository
import com.petid.domain.repository.ReservationCalendarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHospitalDetailUseCase @Inject constructor(
    private val reservationCalendarRepository: ReservationCalendarRepository,
    private val hospitalMainRepository: HospitalMainRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(hospitalId: Long) =
        reservationCalendarRepository
            .getHospitalDetailById(hospitalId)
            .flatMapLatest { result ->
                if (result.imageUrl.isNotEmpty()) {
                    hospitalMainRepository.getHospitalImageUrl(result.imageUrl.first())
                        .map { updatedImageUrl ->
                            result.copy(imageUrl = listOf(updatedImageUrl))
                        }
                } else {
                    flowOf(result)
                }
            }.flowOn(Dispatchers.IO)
}