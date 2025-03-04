package com.petid.petid.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.data.repository.local.DBResult
import com.petid.data.repository.local.NotificationRepository
import com.petid.data.source.local.entity.NotificationEntity
import com.petid.petid.ui.state.CommonUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
): ViewModel() {
    private val _notificationListState = MutableStateFlow<CommonUIState<List<NotificationEntity>>>(
        CommonUIState.Init
    )
    val notificationListState = _notificationListState.asStateFlow()

    /**
     * 알람 목록 가져오기
     */
    fun getNotificationList() {
        viewModelScope.launch {
            val state = when(val result = notificationRepository.getNotifications()) {
                is DBResult.Success -> {
                    val notiList = result.data
                    CommonUIState.Success(notiList)
                }
                is DBResult.Error -> {
                    CommonUIState.Error(result.exception.message)
                }
            }
            _notificationListState.emit(state)
        }
    }

    fun markAsChecked(id: Long) {
        viewModelScope.launch {
            val state = when(val result = notificationRepository.markAsChecked(id)) {
                is DBResult.Success -> {
                    val notiList = result.data
                    CommonUIState.Success(notiList)
                }
                is DBResult.Error -> {
                    CommonUIState.Error(result.exception.message)
                }
            }
            _notificationListState.emit(state)
        }
    }
}