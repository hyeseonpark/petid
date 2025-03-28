package com.petid.domain.repository

import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.domain.util.ApiResult
import kotlinx.coroutines.flow.Flow

interface HospitalMainRepository {
    /**
 * Retrieves a list of primary administrative divisions (sido).
 *
 * This suspend function fetches the top-level location entities representing administrative divisions.
 * The results are encapsulated in an [ApiResult] that indicates either a successful list retrieval
 * or an error state.
 *
 * @return an [ApiResult] containing a list of [LocationEntity] objects.
 */
suspend fun getSido(): ApiResult<List<LocationEntity>>
    suspend fun getSigunguList(id: Int): ApiResult<List<LocationEntity>>
    suspend fun getEupmundongList(id: Int): ApiResult<List<LocationEntity>>

    suspend fun getHospitalList(sidoId: Int, sigunguId: Int, eupmundongId: Int?): ApiResult<List<HospitalEntity>>
    /**
                                    * Retrieves a list of hospital entities based on administrative identifiers and geographic coordinates.
                                    *
                                    * This suspend function filters hospitals using the primary division (sido), secondary division (sigungu),
                                    * an optional tertiary division (eupmundong), and the specified latitude and longitude for additional location context.
                                    *
                                    * @param sidoId Identifier for the primary administrative division.
                                    * @param sigunguId Identifier for the secondary administrative division.
                                    * @param eupmundongId Optional identifier for the tertiary administrative division.
                                    * @param lat The latitude used for geographic filtering.
                                    * @param lon The longitude used for geographic filtering.
                                    * @return An [ApiResult] containing a list of hospital entities that match the criteria.
                                    */
                                   suspend fun getHospitalListLoc(sidoId: Int, sigunguId: Int, eupmundongId: Int?,
                                   lat: Double, lon: Double): ApiResult<List<HospitalEntity>>

    /**
 * Retrieves the URL of the hospital image as a Flow based on the provided file path.
 *
 * @param filePath The file path for the hospital image.
 * @return A Flow emitting the hospital image URL.
 */
suspend fun getHospitalImageUrl(filePath: String): Flow<String>
}