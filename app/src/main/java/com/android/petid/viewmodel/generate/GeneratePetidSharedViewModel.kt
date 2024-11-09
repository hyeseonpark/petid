package com.android.petid.viewmodel.generate

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.data.dto.request.PetAppearanceRequest
import com.android.data.dto.request.PetRequest
import com.android.data.dto.response.UpdateMemberInfoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GeneratePetidSharedViewModel @Inject constructor(

): ViewModel() {
    val tempPetInfoMap = mutableMapOf<String, Any>()

    var petInfo = PetRequest.Builder()
    var memberInfo = UpdateMemberInfoResponse("","","","")

    /**
     * 애완동물 등록
     */
    fun createPetid() {
        Log.d("GeneratePetidSharedViewModel", petInfo.toString())
    }

}