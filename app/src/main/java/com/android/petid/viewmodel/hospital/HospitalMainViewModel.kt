package com.android.petid.viewmodel.hospital

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.HospitalEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.usecase.hospital.GetEupmundongUseCase
import com.android.domain.usecase.hospital.GetHospitalListUseCase
import com.android.domain.usecase.hospital.GetSidoUseCase
import com.android.domain.usecase.hospital.GetSigunguUseCase
import com.android.domain.util.ApiResult
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HospitalMainViewModel @Inject constructor(
    private val getSidoUseCase: GetSidoUseCase,
    private val getSigunguUseCase: GetSigunguUseCase,
    private val getEupmundongUseCase: GetEupmundongUseCase,
    private val getHospitalListUseCase: GetHospitalListUseCase,
    private val savedStateHandle: SavedStateHandle
    ): ViewModel() {

    /**
     * 시,도 api 호출 결과
     */
    private val _sidoApiState = MutableStateFlow<CommonApiState<List<LocationEntity>>>(
        CommonApiState.Loading
    )
    val sidoApiState: StateFlow<CommonApiState<List<LocationEntity>>> = _sidoApiState
    var currentSidoList: List<LocationEntity>? = null

    /**
     * 시,군,구 api 호출 결과
     */
    private val _sigunguApiState = MutableStateFlow<CommonApiState<List<LocationEntity>>>(
        CommonApiState.Loading
    )
    val sigunguApiState: StateFlow<CommonApiState<List<LocationEntity>>> = _sigunguApiState
    var currentSigunguList: List<LocationEntity>? = null

    /**
     * 읍,면,동 api 호출 결과
     */
    private val _eupmundongApiState = MutableStateFlow<CommonApiState<List<LocationEntity>>>(
        CommonApiState.Loading
    )
    val eupmundongApiState: StateFlow<CommonApiState<List<LocationEntity>>> = _eupmundongApiState
    var currentEupmundongList: List<LocationEntity>? = null

    /**
     * 병원 List api 호출 결과
     */
    private val _hospitalApiState = MutableStateFlow<CommonApiState<List<HospitalEntity>>>(
        CommonApiState.Loading
    )
    val hospitalApiState: StateFlow<CommonApiState<List<HospitalEntity>>> = _hospitalApiState

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
            when (val result = getSidoUseCase()) {
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
    fun getSigunguList() {
        viewModelScope.launch {
            when (val result = getSigunguUseCase(currentSidoState.value!!.id)) {
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
    fun getEupmundongList() {
        viewModelScope.launch {
            when (val result = getEupmundongUseCase(currentSigunguState.value!!.id)) {
                is ApiResult.Success -> {
                    _sigunguApiState.emit(CommonApiState.Success(result.data))
                    currentEupmundongList = result.data

                    result.data.firstOrNull()?.let { firstEupmundong ->
                        _currentEupmondongState.emit(firstEupmundong)
                        getHospitalList()
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
        getHospitalList()
    }

    /**
     * 병원 목록 조회
     */
    fun getHospitalList() {
        viewModelScope.launch {
            when (val result = getHospitalListUseCase(
                currentSidoState.value!!.id,
                currentSigunguState.value!!.id,
                currentEupmundongState.value!!.id)) {
                is ApiResult.Success -> {
                    _hospitalApiState.emit(CommonApiState.Success(result.data))
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
}