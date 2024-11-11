package com.android.petid.viewmodel.generate

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.data.dto.request.PetRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GeneratePetidSharedViewModel @Inject constructor(

): ViewModel() {
    var petInfo = PetRequest.Builder()

    /**
     * 애완동물 등록
     */
    fun createPetid() {
        Log.d("GeneratePetidSharedViewModel", petInfo.toString())
    }

}