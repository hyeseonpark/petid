package com.android.petid.viewmodel.hospital

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.HospitalEntity
import com.android.domain.entity.HospitalOrderEntity
import com.android.domain.repository.ReservationCalendarRepository
import com.android.domain.util.ApiResult
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val reservationCalendarRepository: ReservationCalendarRepository
): ViewModel() {

    lateinit var hospitalDetail : HospitalEntity

    // 예약 가능 시간 목록 조회 api request params
    var hospitalId: Int = -1
        get() = if (::hospitalDetail.isInitialized) hospitalDetail.id else -1

    var day: String = ""
    var dateStr: String = ""

    lateinit var selectedDateTime: Date

    /**
     * 예약 가능 시간 목록 state
     */
    private val _hospitalOrderTimeApiState = MutableStateFlow<CommonApiState<List<String>>>(
        CommonApiState.Init
    )
    val hospitalOrderTimeApiState = _hospitalOrderTimeApiState.asStateFlow()

    /**
     * 예약 생성 state
     */
    private val _createHospitalOrderApiState = MutableStateFlow<CommonApiState<HospitalOrderEntity>>(
        CommonApiState.Init
    )
    val createHospitalOrderApiState = _createHospitalOrderApiState.asStateFlow()



    /**
     * 예약 가능 시간 목록 조회
     */
    fun getHospitalOrderTimeList() {
        viewModelScope.launch {
            _hospitalOrderTimeApiState.emit(CommonApiState.Loading)
            val state = when(val result =
                reservationCalendarRepository.getHospitalOrderTimeList(hospitalId, day, dateStr)) {
                    is ApiResult.Success -> CommonApiState.Success(result.data)
                    is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                    is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _hospitalOrderTimeApiState.emit(state)
        }
    }

    /**
     * 예약 생성
     */
    fun createHospitalOrder() {
        viewModelScope.launch {
            _createHospitalOrderApiState.emit(CommonApiState.Loading)
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREAN)
            val formatDateTime = sdf.format(selectedDateTime)
            val state = when(val result = reservationCalendarRepository.createHospitalOrder(
                hospitalId, formatDateTime)) {
                is ApiResult.Success -> CommonApiState.Success(result.data)
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            _createHospitalOrderApiState.emit(state)
        }
    }
}