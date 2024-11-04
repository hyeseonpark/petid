package com.android.petid.viewmodel.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.ContentLikeEntity
import com.android.domain.entity.MemberInfoEntity
import com.android.domain.usecase.my.GetMemberInfoUseCase
import com.android.domain.util.ApiResult
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
): ViewModel() {

    private val _getMemberInfoResult = MutableSharedFlow<CommonApiState<MemberInfoEntity>>()
    val getMemberInfoResult: SharedFlow<CommonApiState<MemberInfoEntity>> = _getMemberInfoResult

    /**
     * member 정보 가져오기
     */
    fun getMemberInfo() {
        // moshi,
        // viewModel에서 repository 바로 연결시키기 (useCase 필요없)
        // .stateIn(
        viewModelScope.launch {
            when (val result = getMemberInfoUseCase()) {
                is ApiResult.Success -> {
                    _getMemberInfoResult.emit(CommonApiState.Success(result.data))
                }
                is ApiResult.HttpError -> {
                    _getMemberInfoResult.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _getMemberInfoResult.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }
}