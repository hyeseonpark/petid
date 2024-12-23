package com.petid.petid.viewmodel.generate

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.data.dto.request.PetRequest
import com.petid.data.dto.request.toDomain
import com.petid.data.util.S3UploadHelper
import com.petid.domain.repository.PetInfoRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.common.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.ui.state.CommonApiState
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

    private val _registerPetResult = MutableStateFlow<CommonApiState<Unit>>(CommonApiState.Init)
    val registerPetResult = _registerPetResult.asStateFlow()

    /**
     * 애완동물 등록
     */
    private fun generatePetid() {
        viewModelScope.launch {
            _registerPetResult.emit(CommonApiState.Loading)
            val state = when (val result = petInfoRepository.registerPet(petInfo.build().toDomain())) {
                is ApiResult.Success -> {
                    CommonApiState.Success(Unit)
                }
                is ApiResult.HttpError -> {
                    CommonApiState.Error(result.error.error)
                }
                is ApiResult.Error -> {
                    CommonApiState.Error(result.errorMessage)
                }
            }
            _registerPetResult.emit(state)
        }
    }

    /**
     * generatePetid를 위해 사전 이미지 업로드
     */
    fun uploadImageFiles() {
        viewModelScope.launch {
            try {
                _registerPetResult.emit(CommonApiState.Loading)
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