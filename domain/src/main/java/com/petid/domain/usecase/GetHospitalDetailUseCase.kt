package com.petid.domain.usecase

import com.petid.domain.repository.HospitalMainRepository
import com.petid.domain.repository.ReservationCalendarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHospitalDetailUseCase @Inject constructor(
    private val reservationCalendarRepository: ReservationCalendarRepository,
    private val hospitalMainRepository: HospitalMainRepository,
) {
    /**
             * Retrieves detailed hospital information for the specified hospital ID with an updated image URL.
             *
             * This function fetches hospital details from the reservation calendar repository and then uses the hospital main
             * repository to retrieve a new image URL based on the original image. The updated image URL replaces the first entry
             * in the image list of the retrieved hospital details. The resulting flow operates on the IO dispatcher.
             *
             * @param hospitalId the unique identifier of the hospital.
             * @return a flow that emits hospital detail objects with the updated image URL.
             */
            @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(hospitalId: Int) =
        reservationCalendarRepository
            .getHospitalDetailById(hospitalId)
            .flatMapLatest { result ->
                hospitalMainRepository.getHospitalImageUrl(result.imageUrl.first())
                    .map { updatedImageUrl ->
                        result.copy(imageUrl = listOf(updatedImageUrl))
                    }
            }.flowOn(Dispatchers.IO)
}