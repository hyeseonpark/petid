package com.android.petid.viewmodel.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.MemberInfoEntity
import com.android.domain.entity.PetDetailsEntity
import com.android.domain.repository.PetInfoRepository
import com.android.domain.util.ApiResult
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetInfoViewModel @Inject constructor(
    private val petInfoRepository: PetInfoRepository
): ViewModel(){

    private val _getPetDetailsResult = MutableStateFlow<CommonApiState<PetDetailsEntity>>(
        CommonApiState.Loading
    )
    val getPetDetailsResult: StateFlow<CommonApiState<PetDetailsEntity>> = _getPetDetailsResult

    /**
     * 펫 정보 가져오기
     */
    fun getPetDetails(petId: Long) {
        viewModelScope.launch {
            when (val result = petInfoRepository.getPetDetails(petId)) {
                is ApiResult.Success -> {
                    val petDetails = result.data
                    //memberInfo.image = memberInfo.image?.let{getMemberImage(it)}

                    _getPetDetailsResult.emit(CommonApiState.Success(petDetails))
                }
                is ApiResult.HttpError -> {
                    _getPetDetailsResult.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _getPetDetailsResult.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }
}