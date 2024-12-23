package com.petid.petid.viewmodel.hospital

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.domain.entity.HospitalOrderDetailEntity
import com.petid.domain.repository.ReservationHistoryInfoRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationHistoryInfoViewModel @Inject constructor(
    private val reservationHistoryInfoRepository: ReservationHistoryInfoRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _hospitalReservationHistoryListApiState = MutableStateFlow<CommonApiState<List<HospitalOrderDetailEntity>>>(
        CommonApiState.Init
    )
    val hospitalReservationHistoryListApiState = _hospitalReservationHistoryListApiState.asStateFlow()

    private val _cancelHospitalReservationApiState = MutableSharedFlow<CommonApiState<Unit>>()
    val cancelHospitalReservationApiState: SharedFlow<CommonApiState<Unit>> = _cancelHospitalReservationApiState

    /**
     * 병원 예약 이력 목록 조회
     */
    fun getHospitalReservationHistoryListApiState() {
        viewModelScope.launch {
            _hospitalReservationHistoryListApiState.emit(CommonApiState.Loading)
            val state = when(val result = reservationHistoryInfoRepository
                .getHospitalReservationHistoryList("ALL")) {
                is ApiResult.Success -> CommonApiState.Success(result.data)
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _hospitalReservationHistoryListApiState.emit(state)
        }
    }

    fun cancelHospitalReservationApiState(orderId: Int) {
        viewModelScope.launch {
            _cancelHospitalReservationApiState.emit(CommonApiState.Loading)
            val state = when (val result = reservationHistoryInfoRepository.cancelHospitalReservation(orderId)) {
                is ApiResult.Success -> CommonApiState.Success(Unit)
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _cancelHospitalReservationApiState.emit(state)
        }
    }
}