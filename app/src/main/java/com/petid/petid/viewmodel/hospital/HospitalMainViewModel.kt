package com.petid.petid.viewmodel.hospital

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.domain.repository.HospitalMainRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.ui.state.CommonApiState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.petid.petid.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HospitalMainViewModel @Inject constructor(
    private val hospitalMainRepository: HospitalMainRepository,
): ViewModel() {

    /* 현재 사용자의 lat, lon 값*/
    var currentLat: Double? = null
    var currentLon: Double? = null

    /* filter: 전체 */
    private val defaultLocationData = LocationEntity(
        -1,
        getGlobalContext().getString(R.string.no_filter)
    )

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
            val state = when (val result = hospitalMainRepository.getSido()) {
                is ApiResult.Success -> {
                    currentSidoList = result.data
                    // 첫 번째 항목을 자동으로 선택
                    result.data.firstOrNull()?.let { firstSido ->
                         _currentSidoState.emit(firstSido)
                        getSigunguList()
                    }

                    CommonApiState.Success(result.data)
                }
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _sidoApiState.emit(state)
        }
    }

    /**
     * 시,군,구 목록 조회
     */
    private fun getSigunguList() {
        viewModelScope.launch {
            val state = when (val result = hospitalMainRepository.getSigunguList(currentSidoState.value!!.id)) {
                is ApiResult.Success -> {
                    currentSigunguList = result.data

                    result.data.firstOrNull()?.let { firstSigungu ->
                        _currentSigunguState.emit(firstSigungu)
                        getEupmundongList()
                    }

                    CommonApiState.Success(result.data)
                }
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _sigunguApiState.emit(state)
        }
    }

    /**
     * 읍,면,동 목록 조회
     */
    private fun getEupmundongList() {
        viewModelScope.launch {
            val state = when (val result = hospitalMainRepository.getEupmundongList(currentSigunguState.value!!.id)) {
                is ApiResult.Success -> {
                    currentEupmundongList = listOf(defaultLocationData) + result.data

                    currentEupmundongList?.firstOrNull()?.let { firstEupmundong ->
                        _currentEupmondongState.emit(firstEupmundong)
                        fetchHospitalList()
                    }

                    CommonApiState.Success(result.data)
                }
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _eupmundongApiState.emit(state)
        }
    }

    /**
     * 시,도 선택 변경 시
     */
    fun updateCurrentSidoState(sido: LocationEntity) {
        _currentSidoState.value = sido
        getSigunguList()
    }

    /**
     * 시,군,구 선택 변경 시
     */
    fun updateCurrentSigunguState(sigungu: LocationEntity) {
        _currentSigunguState.value = sigungu
        _currentEupmondongState.value = defaultLocationData

        fetchHospitalList()
    }

    /**
     * 읍,면,동 선택 변경 시
     */
    fun updateCurrentEupmundongState(eupmundong: LocationEntity) {
        _currentEupmondongState.value = eupmundong
        fetchHospitalList()
    }

    /**
     * 조건에 따라 병원 목록 가져오기
     */
    private fun fetchHospitalList() =
        when(currentLat == null && currentLon == null) {
            true -> getHospitalList()
            false -> getHospitalListByLocation()
        }

    /**
     * 병원 목록 조회
     */
    private fun getHospitalList() {
        viewModelScope.launch {
            _hospitalApiState.emit(CommonApiState.Loading)
            val state = when (val result = hospitalMainRepository.getHospitalList(
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

                    CommonApiState.Success(hospitalList)
                }
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _hospitalApiState.emit(state)
        }
    }

    /**
     * 병원 목록 조회 (거리 순 정렬)
     */
    private fun getHospitalListByLocation() {
        viewModelScope.launch {
            _hospitalApiState.emit(CommonApiState.Loading)
            val state = when (val result = hospitalMainRepository.getHospitalListLoc(
                currentSidoState.value!!.id,
                currentSigunguState.value!!.id,
                currentEupmundongState.value?.id,
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

                    CommonApiState.Success(hospitalList)
                }
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _hospitalApiState.emit(state)
        }
    }

    /**
     * 병원 이미지 가져오기
     */
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