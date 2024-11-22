package com.android.petid.viewmodel.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.PetDetailsEntity
import com.android.domain.repository.PetInfoRepository
import com.android.domain.util.ApiResult
import com.android.petid.common.Constants
import com.android.petid.ui.state.CommonApiState
import com.android.petid.util.PreferencesControl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetInfoViewModel @Inject constructor(
    private val petInfoRepository: PetInfoRepository,
    private val preferencesControl: PreferencesControl
): ViewModel(){

    /* 펫 정보 가져오기 결과*/
    private val _getPetDetailsResult = MutableStateFlow<CommonApiState<PetDetailsEntity>>(
        CommonApiState.Init
    )
    val getPetDetailsResult = _getPetDetailsResult.asStateFlow()

    /* 펫 이미지 가져오기 결과*/
    private val _getPetImageUrlResult = MutableStateFlow<CommonApiState<String>>(
        CommonApiState.Init
    )
    val getPetImageUrlResult = _getPetImageUrlResult.asStateFlow()

    /**
     * 펫 정보 가져오기
     */
    fun getPetDetails() {
        viewModelScope.launch {
            val petId = preferencesControl.getIntValue(Constants.SHARED_PET_ID_VALUE).toLong()
            when (val result = petInfoRepository.getPetDetails(petId)) {
                is ApiResult.Success -> {
                    val petDetails = result.data
                    getPetImageUrl(petDetails.petImages[0].imagePath)

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

    /**
     * 펫 이미지 S3 주소 가져오기
     */
    private fun getPetImageUrl(filePath: String) {
        viewModelScope.launch {
            when (val result = petInfoRepository.getPetImageUrl(filePath)) {
                is ApiResult.Success -> {
                    _getPetImageUrlResult.emit(CommonApiState.Success(result.data))
                }
                is ApiResult.HttpError -> {
                    _getPetImageUrlResult.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _getPetImageUrlResult.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }
}