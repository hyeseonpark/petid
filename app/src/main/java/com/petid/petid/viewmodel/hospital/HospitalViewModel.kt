package com.petid.petid.viewmodel.hospital

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.HospitalOrderEntity
import com.petid.domain.repository.ReservationCalendarRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.ui.state.CommonUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val _hospitalOrderTimeApiState = MutableStateFlow<CommonUIState<List<String>>>(
        CommonUIState.Init
    )
    val hospitalOrderTimeApiState = _hospitalOrderTimeApiState.asStateFlow()

    /**
     * 예약 생성 state
     */
    private val _createHospitalOrderApiState = MutableStateFlow<CommonUIState<HospitalOrderEntity>>(
        CommonUIState.Init
    )
    val createHospitalOrderApiState = _createHospitalOrderApiState.asStateFlow()



    /**
     * 예약 가능 시간 목록 조회
     */
    fun getHospitalOrderTimeList() {
        viewModelScope.launch {
            _hospitalOrderTimeApiState.emit(CommonUIState.Loading)
            val state = when(val result =
                reservationCalendarRepository.getHospitalOrderTimeList(hospitalId, day, dateStr)) {
                    is ApiResult.Success -> CommonUIState.Success(result.data)
                    is ApiResult.HttpError -> CommonUIState.Error(result.error.error)
                    is ApiResult.Error -> CommonUIState.Error(result.errorMessage)
            }
            _hospitalOrderTimeApiState.emit(state)
        }
    }

    /**
     * 예약 생성
     */
    fun createHospitalOrder() {
        viewModelScope.launch {
            _createHospitalOrderApiState.emit(CommonUIState.Loading)
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.KOREAN)
            val formatDateTime = sdf.format(selectedDateTime)

            val data = HospitalOrderEntity(hospitalId, formatDateTime)
            val state =
                when(val result = reservationCalendarRepository.createHospitalOrder(data)) {
                    is ApiResult.Success -> CommonUIState.Success(result.data)
                    is ApiResult.HttpError -> CommonUIState.Error(result.error.error)
                    is ApiResult.Error -> CommonUIState.Error(result.errorMessage)
                }
            _createHospitalOrderApiState.emit(state)
        }
    }
}