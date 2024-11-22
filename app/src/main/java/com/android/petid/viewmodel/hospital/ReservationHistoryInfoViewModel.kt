package com.android.petid.viewmodel.hospital

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.HospitalOrderDetailEntity
import com.android.domain.repository.ReservationHistoryInfoRepository
import com.android.domain.usecase.hospital.ReservationHistoryInfoUseCase
import com.android.domain.util.ApiResult
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationHistoryInfoViewModel @Inject constructor(
    private val reservationHistoryInfoUseCase: ReservationHistoryInfoUseCase,
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

    fun cancelHospitalReservationApiState(orderId: Int) {
        viewModelScope.launch {
            when (val result = reservationHistoryInfoRepository.cancelHospitalReservation(orderId)) {
                is ApiResult.Success -> {
                    _cancelHospitalReservationApiState.emit(CommonApiState.Success(Unit))
                }
                is ApiResult.HttpError -> {
                    _cancelHospitalReservationApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _cancelHospitalReservationApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }
}