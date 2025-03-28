package com.petid.data.source.remote

import com.petid.data.api.HospitalAPI
import com.petid.data.dto.request.HospitalOrderRequest
import com.petid.data.dto.response.HospitalOrderResponse
import com.petid.data.dto.response.HospitalResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReservationCalendarRemoteDataSourceImpl @Inject constructor(
    private val hospitalAPI: HospitalAPI
): ReservationCalendarRemoteDataSource {
    override suspend fun getHospitalOrderTimeList(
        hospitalId: Int,
        day: String,
        date: String
    ): List<String> =
        hospitalAPI.getHospitalOrderTimeList(hospitalId, day, date)

    /**
         * Creates a hospital order.
         *
         * This suspend function sends the provided order request to the remote hospital API and returns the corresponding order details.
         *
         * @param hospitalOrderRequest the order details required to create the hospital order.
         * @return the response detailing the created hospital order.
         */
        override suspend fun createHospitalOrder(
        hospitalOrderRequest: HospitalOrderRequest,
    ): HospitalOrderResponse =
        hospitalAPI.createHospitalOrder(hospitalOrderRequest)

    /**
         * Retrieves detailed information about a hospital by its ID.
         *
         * @param id the unique identifier of the hospital.
         * @return a [HospitalResponse] containing the hospital's detailed information.
         */
        override suspend fun getHospitalDetailById(id: Int): HospitalResponse =
        hospitalAPI.getHospitalDetailById(id)
}