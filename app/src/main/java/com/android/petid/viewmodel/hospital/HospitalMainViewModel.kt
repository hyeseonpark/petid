package com.android.petid.viewmodel.hospital

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.HospitalEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.repository.HospitalMainRepository
import com.android.domain.util.ApiResult
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.ui.state.CommonApiState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class HospitalMainViewModel @Inject constructor(
    private val hospitalMainRepository: HospitalMainRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    /* 현재 사용자의 lat, lon 값*/
    var currentLat: Double? = null
    var currentLon: Double? = null

    /**
     * 시,도 api 호출 결과
     */
    private val _sidoApiState = MutableStateFlow<CommonApiState<List<LocationEntity>>>(
        CommonApiState.Init
    )
    val sidoApiState = _sidoApiState.asStateFlow()
    var currentSidoList: List<LocationEntity>? = null

    /**
     * 시,군,구 api 호출 결과
     */
    private val _sigunguApiState = MutableStateFlow<CommonApiState<List<LocationEntity>>>(
        CommonApiState.Init
    )
    val sigunguApiState = _sigunguApiState.asStateFlow()
    var currentSigunguList: List<LocationEntity>? = null

    /**
     * 읍,면,동 api 호출 결과
     */
    private val _eupmundongApiState = MutableStateFlow<CommonApiState<List<LocationEntity>>>(
        CommonApiState.Init
    )
    val eupmundongApiState = _eupmundongApiState.asStateFlow()
    var currentEupmundongList: List<LocationEntity>? = null

    /**
     * 병원 List api 호출 결과
     */
    private val _hospitalApiState = MutableStateFlow<CommonApiState<List<HospitalEntity>>>(
        CommonApiState.Init
    )
    val hospitalApiState = _hospitalApiState.asStateFlow()

    /**
     * 현재 선택된 시/도 및 시/군/구 및 읍/면/동
     */
    private var _currentSidoState = MutableStateFlow<LocationEntity?>(null)
    var currentSidoState: StateFlow<LocationEntity?> = _currentSidoState

    private var _currentSigunguState = MutableStateFlow<LocationEntity?>(null)
    var currentSigunguState: StateFlow<LocationEntity?> = _currentSigunguState

    private var _currentEupmondongState = MutableStateFlow<LocationEntity?>(null)
    var currentEupmundongState: StateFlow<LocationEntity?> = _currentEupmondongState



    /*init {
        Log.d("temp", "hospital main view model init...")
        getSido()
    }*/

    /**
     * 시, 도 목록 조회
     */
    fun getSidoList() {
        viewModelScope.launch {
            when (val result = hospitalMainRepository.getSido()) {
                is ApiResult.Success -> {
                    _sidoApiState.emit(CommonApiState.Success(result.data))
                    currentSidoList = result.data
                    // 첫 번째 항목을 자동으로 선택
                    result.data.firstOrNull()?.let { firstSido ->
                         _currentSidoState.emit(firstSido)
                        getSigunguList()
                    }
                }
                is ApiResult.HttpError -> {
                    _sidoApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _sidoApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }

    /**
     * 시,군,구 목록 조회
     */
    private fun getSigunguList() {
        viewModelScope.launch {
            when (val result = hospitalMainRepository.getSigunguList(currentSidoState.value!!.id)) {
                is ApiResult.Success -> {
                    _sigunguApiState.emit(CommonApiState.Success(result.data))
                    currentSigunguList = result.data

                    result.data.firstOrNull()?.let { firstSigungu ->
                        _currentSigunguState.emit(firstSigungu)
                        getEupmundongList()
                    }
                }
                is ApiResult.HttpError -> {
                    _sigunguApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _sigunguApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }

    /**
     * 읍,면,동 목록 조회
     */
    private fun getEupmundongList() {
        viewModelScope.launch {
            when (val result = hospitalMainRepository.getEupmundongList(currentSigunguState.value!!.id)) {
                is ApiResult.Success -> {
                    _sigunguApiState.emit(CommonApiState.Success(result.data))
                    currentEupmundongList = result.data

                    result.data.firstOrNull()?.let { firstEupmundong ->
                        _currentEupmondongState.emit(firstEupmundong)

                        when(currentLat == null && currentLon == null) {
                            true -> getHospitalList()
                            false -> getHospitalListByLocation()
                        }
                    }
                }
                is ApiResult.HttpError -> {
                    _eupmundongApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _eupmundongApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }

    fun updateCurrentSidoState(sido: LocationEntity) {
        _currentSidoState.value = sido
        getSigunguList()
    }

    fun updateCurrentSigunguState(sigungu: LocationEntity) {
        _currentSigunguState.value = sigungu
        getEupmundongList()
    }

    fun updateCurrentEupmundongState(eupmundong: LocationEntity) {
        _currentEupmondongState.value = eupmundong

        // 분기처리
        when(currentLat == null && currentLon == null) {
            true -> getHospitalList()
            false -> getHospitalListByLocation()
        }
    }

    /**
     * 병원 목록 조회
     */
    private fun getHospitalList() {
        viewModelScope.launch {
            when (val result = hospitalMainRepository.getHospitalList(
                currentSidoState.value!!.id,
                currentSigunguState.value!!.id,
                currentEupmundongState.value!!.id)) {
                is ApiResult.Success -> {
                    var hospitalList = result.data

                    hospitalList = hospitalList.map { item ->
                        val hospitalImageUrl = when (item.imageUrl.size) {
                            0 -> ""
                            else -> getHospitalImage(item.imageUrl[0]) // 배열의 첫번째 사진
                        }
                        item.copy(imageUrl = listOf(hospitalImageUrl))
                    }

                    _hospitalApiState.emit(CommonApiState.Success(hospitalList))
                }
                is ApiResult.HttpError -> {
                    _hospitalApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _hospitalApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }

    /**
     * 병원 목록 조회 (거리 순 정렬)
     */
    private fun getHospitalListByLocation() {
        viewModelScope.launch {
            when (val result = hospitalMainRepository.getHospitalListLoc(
                currentSidoState.value!!.id,
                currentSigunguState.value!!.id,
                currentEupmundongState.value!!.id,
                currentLat!!, currentLon!!)) {
                is ApiResult.Success -> {
                    var hospitalList = result.data

                    hospitalList = hospitalList.map { item ->
                        val hospitalImageUrl = when (item.imageUrl.size) {
                            0 -> ""
                            else -> getHospitalImage(item.imageUrl[0]) // 배열의 첫번째 사진
                        }
                        item.copy(imageUrl = listOf(hospitalImageUrl))
                    }

                    _hospitalApiState.emit(CommonApiState.Success(hospitalList))
                }
                is ApiResult.HttpError -> {
                    _hospitalApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _hospitalApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }

    private suspend fun getHospitalImage(filePath: String): String {
        return try {
            hospitalMainRepository.getHospitalImageUrl(filePath)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 현재 위치 가져오기
     */
    @SuppressLint("MissingPermission")
    fun getSingleLocation(){
        viewModelScope.launch {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(getGlobalContext())

            fusedLocationClient.lastLocation.addOnSuccessListener { lastLocation ->
                if (lastLocation != null) {
                    updateLocation(lastLocation.latitude, lastLocation.longitude)
                } else {
                    fetchCurrentLocation(fusedLocationClient)
                }
            }
        }
    }

    /**
     * 위치정보 업데이트
     */
    private fun updateLocation(latitude: Double, longitude: Double) {
        currentLat = latitude
        currentLon = longitude
        getSidoList()
    }

    /**
     * FusedLocationProviderClient 을 이용한 위치 정보 가져오기
     */
    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation(fusedLocationClient: FusedLocationProviderClient) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11(API 30) 이상
            fusedLocationClient.getCurrentLocation(
                LocationRequest.QUALITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    updateLocation(location.latitude, location.longitude)
                } else {
                    fetchLocationUsingManager()
                }
            }
        } else {
            // Android 10 이하
            fetchLocationUsingManager()
        }
    }

    /**
     * locationManager 을 이용한 위치 정보 가져오기
     */
    @SuppressLint("MissingPermission")
    private fun fetchLocationUsingManager() {
        val locationManager = getGlobalContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val provider = locationManager.getBestProvider(Criteria(), true)

        if (provider != null) {
            val location = locationManager.getLastKnownLocation(provider)
            if (location != null) {
                updateLocation(location.latitude, location.longitude)
            } else {
                // 위치 정보를 가져올 수 없는 경우
                getSidoList()
            }
        }
    }
}