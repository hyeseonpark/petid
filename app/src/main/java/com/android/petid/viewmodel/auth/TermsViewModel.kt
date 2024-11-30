package com.android.petid.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.usecase.login.DoJoinUseCase
import com.android.domain.util.ApiResult
import com.android.petid.common.Constants.SHARED_VALUE_ACCESS_TOKEN
import com.android.petid.common.Constants.SHARED_VALUE_REFRESH_TOKEN
import com.android.petid.enum.PlatformType
import com.android.petid.ui.state.CommonApiState
import com.android.petid.util.PreferencesControl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val doJoinUseCase: DoJoinUseCase,
    private val preferencesControl: PreferencesControl,
//    private val savedStateHandle: SavedStateHandle,
    ): ViewModel() {

    private val _apiState = MutableSharedFlow<CommonApiState<Unit>>()  // Unit을 사용한 이유는 join 결과를 별도로 받을 필요가 없기 때문입니다.
    val apiState: SharedFlow<CommonApiState<Unit>> = _apiState

        fun join(platform: PlatformType, sub: String, fcmToken: String, ad: Boolean) {
            viewModelScope.launch {
                _apiState.emit(CommonApiState.Loading)  // 로딩 상태 전송
                when (val result = doJoinUseCase(platform.toString(), sub, fcmToken, ad)) {
                    is ApiResult.Success -> {
                        val result = result.data
                        preferencesControl.apply {
                            saveStringValue(SHARED_VALUE_ACCESS_TOKEN, result.accessToken)
                            saveStringValue(SHARED_VALUE_REFRESH_TOKEN, result.refreshToken)
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