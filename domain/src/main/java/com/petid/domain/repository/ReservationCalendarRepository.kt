package com.petid.domain.repository

import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.HospitalOrderEntity
import com.petid.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface ReservationCalendarRepository {
    /**
 * Retrieves available order times for a hospital on a specified day and date.
 *
 * This suspend function returns an [ApiResult] encapsulating a list of order times based on the hospital's ID,
 * the day of the week, and the full calendar date.
 *
 * @param hospitalId the unique identifier of the hospital.
 * @param day the day of the week (e.g., "Monday") for which order times are requested.
 * @param date the specific calendar date (e.g., "2025-03-25") to filter available order times.
 * @return an [ApiResult] containing a list of order time strings.
 */
suspend fun getHospitalOrderTimeList(hospitalId: Int, day: String, date: String): ApiResult<List<String>>
    /**
 * Creates a new hospital order.
 *
 * This function creates a hospital order based on the provided order details and returns the result wrapped in an [ApiResult].
 *
 * @param hospitalOrderEntity the details of the hospital order to be created.
 * @return an [ApiResult] containing the created hospital order entity.
 */
suspend fun createHospitalOrder(hospitalOrderEntity: HospitalOrderEntity): ApiResult<HospitalOrderEntity>
    /**
 * Retrieves the details of a hospital by its unique identifier.
 *
 * This function returns a Flow that emits the corresponding HospitalEntity, allowing for reactive updates.
 *
 * @param id the hospital's unique identifier.
 * @return a Flow emitting the corresponding HospitalEntity.
 */
suspend fun getHospitalDetailById(id: Int): Flow<HospitalEntity>
}