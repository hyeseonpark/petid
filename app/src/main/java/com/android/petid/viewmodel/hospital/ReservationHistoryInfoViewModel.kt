package com.android.petid.viewmodel.hospital

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.HospitalOrderDetailEntity
import com.android.domain.usecase.hospital.ReservationHistoryInfoUseCase
import com.android.domain.util.ApiResult
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationHistoryInfoViewModel @Inject constructor(
    private val reservationHistoryInfoUseCase: ReservationHistoryInfoUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _hospitalReservationHistoryListApiState = MutableStateFlow<CommonApiState<List<HospitalOrderDetailEntity>>>(
        CommonApiState.Loading
    )
    val hospitalReservationHistoryListApiState: StateFlow<CommonApiState<List<HospitalOrderDetailEntity>>> = _hospitalReservationHistoryListApiState

    /**
     * 병원 예약 이력 목록 조회
     */
    fun getHospitalReservationHistoryListApiState() {
        viewModelScope.launch {
            when(val result = reservationHistoryInfoUseCase("ALL")) {
                is ApiResult.Success -> {
                    _hospitalReservationHistoryListApiState.emit(CommonApiState.Success(result.data))

                }
                is ApiResult.HttpError -> {
                    _hospitalReservationHistoryListApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _hospitalReservationHistoryListApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }
}