package com.android.petid.viewmodel.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.MemberInfoEntity
import com.android.domain.repository.MyInfoRepository
import com.android.domain.util.ApiResult
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val myInfoRepository: MyInfoRepository,
): ViewModel() {

    private val _getMemberInfoResult = MutableStateFlow<CommonApiState<MemberInfoEntity>>(
        CommonApiState.Loading
    )
    val getMemberInfoResult: StateFlow<CommonApiState<MemberInfoEntity>> = _getMemberInfoResult

    /**
     * member 정보 가져오기
     */
    fun getMemberInfo() {
        // TODO 1. moshi, 2.viewModel에서 repository 바로 연결시키기 (useCase 필요없), 3.stateIn()
        viewModelScope.launch {
            when (val result = myInfoRepository.getMemberInfo()) {
                is ApiResult.Success -> {
                    val memberInfo = result.data
                    memberInfo.image = memberInfo.image?.let{getMemberImage(it)}

                    _getMemberInfoResult.emit(CommonApiState.Success(memberInfo))
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

    /**
     * 프로필 사진 가져오기
     */
    private suspend fun getMemberImage(imageUrl: String): String {
        return try {
            myInfoRepository.getProfileImageUrl(imageUrl)
        } catch (e: Exception) {
            ""
        }
    }

}