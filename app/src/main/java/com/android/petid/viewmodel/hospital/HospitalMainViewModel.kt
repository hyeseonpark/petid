package com.android.petid.viewmodel.hospital

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.LocationEntity
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
     * 현재 선택된 시/도 및 시/군/구
     */
    private var _currentSidoState = MutableStateFlow<LocationEntity?>(null)
    var currentSidoState: StateFlow<LocationEntity?> = _currentSidoState

    private var _currentSigunguState = MutableStateFlow<LocationEntity?>(null)
    var currentSigunguState: StateFlow<LocationEntity?> = _currentSigunguState



    /*init {
        Log.d("temp", "hospital main view model init...")
        getSido()
    }*/

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
    fun getSigunguList() {
        viewModelScope.launch {
            when (val result = getSigunguUseCase(currentSidoState.value!!.id)) {
                is ApiResult.Success -> {
                    _sigunguApiState.emit(CommonApiState.Success(result.data))
                    currentSigunguList = result.data

                    result.data.firstOrNull()?.let { firstSido ->
                        _currentSigunguState.emit(firstSido)
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

    fun updateCurrentSidoState(sido: LocationEntity) {
        _currentSidoState.value = sido
        getSigunguList()
    }

    fun updateCurrentSigunguState(sigungu: LocationEntity) {
        _currentSigunguState.value = sigungu
    }
}