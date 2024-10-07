package com.android.petid.viewmodel.hospital

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.HospitalOrderEntity
import com.android.domain.usecase.hospital.CreateHospitalOrderUseCase
import com.android.domain.usecase.hospital.GetHospitalOrderTimeListUseCase
import com.android.domain.util.ApiResult
import com.android.petid.ui.state.CommonApiState
import com.android.petid.util.formatDateToISO8601
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ReservationCalendarViewModel @Inject constructor(
    private val getHospitalOrderTimeListUseCase: GetHospitalOrderTimeListUseCase,
    private val createHospitalOrderUseCase: CreateHospitalOrderUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    // 예약 가능 시간 목록 조회 api request params
    var hospitalId: Int = -1
    var day: String = ""
    var dateStr: String = ""

    lateinit var selectedDateTime: Date

    /**
     *
     */
    private val _hospitalOrderTimeApiState = MutableStateFlow<CommonApiState<List<String>>>(
        CommonApiState.Loading
    )
    val hospitalOrderTimeApiState: StateFlow<CommonApiState<List<String>>> = _hospitalOrderTimeApiState

    /**
     *
     */
    private val _createHospitalOrderApiState = MutableStateFlow<CommonApiState<HospitalOrderEntity>>(
        CommonApiState.Loading
    )
    val createHospitalOrderApiState: StateFlow<CommonApiState<HospitalOrderEntity>> = _createHospitalOrderApiState



    /**
     * 예약 가능 시간 목록 조회
     */
    fun getHospitalOrderTimeList() {
        viewModelScope.launch {
            when(val result = getHospitalOrderTimeListUseCase(hospitalId, day, dateStr)) {
                is ApiResult.Success -> {
                    _hospitalOrderTimeApiState.emit(CommonApiState.Success(result.data))

                }
                is ApiResult.HttpError -> {
                    _hospitalOrderTimeApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _hospitalOrderTimeApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }

    /**
     * 예약 생성
     */
    fun createHospitalOrder() {
        viewModelScope.launch {
            when(val result = createHospitalOrderUseCase(
                hospitalId, selectedDateTime)) {
                is ApiResult.Success -> {
                    _createHospitalOrderApiState.emit(CommonApiState.Success(result.data))
                }
                is ApiResult.HttpError -> {
                    _createHospitalOrderApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _createHospitalOrderApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }
}