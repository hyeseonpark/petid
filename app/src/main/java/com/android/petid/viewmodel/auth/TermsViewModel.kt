package com.android.petid.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.repository.TermsRepository
import com.android.domain.util.ApiResult
import com.android.petid.common.Constants.SHARED_VALUE_ACCESS_TOKEN
import com.android.petid.common.Constants.SHARED_VALUE_REFRESH_TOKEN
import com.android.petid.common.GlobalApplication.Companion.getPreferencesControl
import com.android.petid.enum.PlatformType
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val termsRepository: TermsRepository,
    ): ViewModel() {

    private val _apiState = MutableSharedFlow<CommonApiState<Unit>>()
    val apiState: SharedFlow<CommonApiState<Unit>> = _apiState

        fun join(platform: PlatformType, sub: String, fcmToken: String, ad: Boolean) {
            viewModelScope.launch {
                _apiState.emit(CommonApiState.Loading)  // 로딩 상태 전송
                when (val result = termsRepository.doJoin(platform.toString(), sub, fcmToken, ad)) {
                    is ApiResult.Success -> {
                        val result = result.data
                        getPreferencesControl().apply {
                            saveStringValue(SHARED_VALUE_ACCESS_TOKEN, result.accessToken.split(" ").last())
                            saveStringValue(SHARED_VALUE_REFRESH_TOKEN, result.refreshToken.split(" ").last())
                        }
                         _apiState.emit(CommonApiState.Success(Unit))  // 성공 시 UI 상태 전송

                    }
                    is ApiResult.HttpError -> {
                        _apiState.emit(CommonApiState.Error(result.error.error))  // 오류 시 메시지 전송
                    }
                    is ApiResult.Error -> {
                        _apiState.emit(CommonApiState.Error(result.errorMessage))
                    }
                }
            }
        }
}