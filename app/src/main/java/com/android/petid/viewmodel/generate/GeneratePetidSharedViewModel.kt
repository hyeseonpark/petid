package com.android.petid.viewmodel.generate

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.data.dto.request.PetRequest
import com.android.data.dto.request.toDomain
import com.android.data.util.S3UploadHelper
import com.android.domain.repository.PetInfoRepository
import com.android.domain.util.ApiResult
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class GeneratePetidSharedViewModel @Inject constructor(
    private val petInfoRepository: PetInfoRepository,
): ViewModel() {
    var petInfo = PetRequest.Builder()
    var petImage : File? = null
    var signImage : File? = null

    /* S3 upload helper 초기화 */
    private val s3UploadHelper = S3UploadHelper()

    private val _registerPetResult = MutableStateFlow<CommonApiState<Boolean>>(CommonApiState.Init)
    val registerPetResult = _registerPetResult.asStateFlow()

    /**
     * 애완동물 등록
     */
    private fun generatePetid() {
        viewModelScope.launch {
            when (val result = petInfoRepository.registerPet(petInfo.build().toDomain())) {
                is ApiResult.Success -> {
                    //val petDetails = result.data
                    //memberInfo.image = memberInfo.image?.let{getMemberImage(it)}

                    _registerPetResult.emit(CommonApiState.Success(true))
                }
                is ApiResult.HttpError -> {
                    _registerPetResult.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _registerPetResult.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }

    /**
     * generatePetid를 위해 사전 이미지 업로드
     */
    fun uploadImageFiles() {
        viewModelScope.launch {
            try {
                if (!handleUploadResult(getGlobalContext(), petImage!!, petInfo.getPetImageName()))
                    return@launch
                if (!handleUploadResult(getGlobalContext(), signImage!!, petInfo.getSignImageName()))
                    return@launch

            } catch (e: Exception) {
                _registerPetResult.emit(CommonApiState.Error(e.message))
            } finally {
                generatePetid()
            }
        }
    }

    /**
     * S3 bucket upload
     */
    private suspend fun handleUploadResult(
        context: Context,
        file: File,
        keyName: String
    ): Boolean {
        return s3UploadHelper.uploadWithTransferUtility(
            context = context,
            file = file,
            scope = viewModelScope,
            keyName = keyName
        ).fold(
            onSuccess = {
                Log.d("ViewModel", "$keyName upload succeeded")
                true
            },
            onFailure = { e ->
                _registerPetResult.emit(CommonApiState.Error(e.message))
                false
            }
        )
    }
}