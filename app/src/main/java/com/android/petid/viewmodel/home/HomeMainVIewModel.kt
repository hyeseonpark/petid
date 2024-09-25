package com.android.petid.viewmodel.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.BannerEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.usecase.main.GetBannerImageUseCase
import com.android.domain.usecase.main.GetBannerListUseCase
import com.android.domain.util.ApiResult
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeMainVIewModel @Inject constructor(
    private val getBannerListUseCase: GetBannerListUseCase,
    private val getBannerImageUseCase: GetBannerImageUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _bannerApiState = MutableStateFlow<CommonApiState<List<BannerEntity>>>(
        CommonApiState.Loading
    )
    val bannerApiState: StateFlow<CommonApiState<List<BannerEntity>>> = _bannerApiState

    private val _bannerImageApiState = MutableStateFlow<CommonApiState<String>>(CommonApiState.Loading)
    val bannerImageApiState: StateFlow<CommonApiState<String>> = _bannerImageApiState

    fun getBannerList(type: String) {
        viewModelScope.launch {
            when (val result = getBannerListUseCase(type)) {
                is ApiResult.Success -> {
                    _bannerApiState.emit(CommonApiState.Success(result.data))
                }
                is ApiResult.HttpError ->{
                    _bannerApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _bannerApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }

    fun getBannerImage(imagePath: String) {
        viewModelScope.launch {
            when (val result = getBannerImageUseCase(imagePath)) {
                is ApiResult.Success -> {
                    _bannerImageApiState.emit(CommonApiState.Success(result.data))
                }
                is ApiResult.HttpError ->{
                    _bannerImageApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _bannerImageApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }
}