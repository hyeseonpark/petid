package com.android.data.repository

import com.android.data.source.remote.HospitalMainRemoteDataSource
import com.android.domain.entity.HospitalEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.repository.HospitalMainRepository
import com.android.domain.util.ApiResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HospitalMainRepositoryImpl @Inject constructor(
    private val remoteDataSource: HospitalMainRemoteDataSource
) : HospitalMainRepository{
    override suspend fun getSido(): ApiResult<List<LocationEntity>> {
        return when (val result = remoteDataSource.getSido()) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun getSigunguList(id: Int): ApiResult<List<LocationEntity>> {
        return when (val result = remoteDataSource.getSigunguList(id)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun getEupmundongList(id: Int): ApiResult<List<LocationEntity>> {
        return when (val result = remoteDataSource.getEupmundongList(id)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }

    override suspend fun getHospitalList(
        sidoId: Int,
        sigunguId: Int,
        eupmundongId: Int
    ): ApiResult<List<HospitalEntity>> {
        return when (val result = remoteDataSource.getHospitalList(sidoId, sigunguId, eupmundongId)) {
            is ApiResult.Success -> result
            is ApiResult.HttpError -> result
            is ApiResult.Error -> result
        }
    }
}