package com.petid.petid.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.data.util.Constants.SHARED_VALUE_ACCESS_TOKEN
import com.petid.data.util.Constants.SHARED_VALUE_REFRESH_TOKEN
import com.petid.domain.repository.TermsRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.GlobalApplication.Companion.getPreferencesControl
import com.petid.petid.type.PlatformType
import com.petid.petid.ui.state.CommonUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val termsRepository: TermsRepository,
    ): ViewModel() {

    private val _apiState = MutableSharedFlow<CommonUIState<Unit>>()
    val apiState: SharedFlow<CommonUIState<Unit>> = _apiState

        fun join(platform: PlatformType, token: String, fcmToken: String, ad: Boolean) {
            viewModelScope.launch {
                _apiState.emit(CommonUIState.Loading)  // 로딩 상태 전송
                val state = when (val result = termsRepository.doJoin(platform.toString(), token, fcmToken, ad)) {
                    is ApiResult.Success -> {
                        val result = result.data
                        getPreferencesControl().apply {
                            saveStringValue(SHARED_VALUE_ACCESS_TOKEN, result.accessToken.split(" ").last())
                            saveStringValue(SHARED_VALUE_REFRESH_TOKEN, result.refreshToken.split(" ").last())
                        }
                         CommonUIState.Success(Unit)
                    }
                    is ApiResult.HttpError -> {
                        CommonUIState.Error(result.error.error)
                    }
                    is ApiResult.Error -> {
                        CommonUIState.Error(result.errorMessage)
                    }
                }
                _apiState.emit(state)
            }
        }
}