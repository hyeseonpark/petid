package com.petid.data.source.remote

import com.petid.data.dto.request.HospitalOrderRequest
import com.petid.data.dto.response.HospitalOrderResponse
import com.petid.data.dto.response.HospitalResponse

interface ReservationCalendarRemoteDataSource {
    /**
 * Retrieves available hospital order times for a specified hospital.
 *
 * @param hospitalId the unique identifier of the hospital.
 * @param day the day for which to retrieve order times.
 * @param date the specific date to query order times.
 * @return a list of strings representing the available order times.
 */
suspend fun getHospitalOrderTimeList(hospitalId: Int, day: String, date: String): List<String>
    /**
 * Creates a hospital order asynchronously.
 *
 * Processes the provided hospital order request and returns a response containing the result of the order creation.
 *
 * @param hospitalOrderRequest the request object with the details necessary to create the hospital order.
 * @return a [HospitalOrderResponse] representing the outcome of the order creation.
 */
suspend fun createHospitalOrder(hospitalOrderRequest: HospitalOrderRequest): HospitalOrderResponse
    /**
 * Retrieves detailed information for the hospital identified by the provided ID.
 *
 * This suspend function calls the remote data source to return a response containing
 * the hospital details.
 *
 * @param id the unique identifier of the hospital.
 * @return a HospitalResponse containing the hospital's details.
 */
suspend fun getHospitalDetailById(id: Int): HospitalResponse
}