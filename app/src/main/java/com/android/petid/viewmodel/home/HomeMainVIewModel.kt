package com.android.petid.viewmodel.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.BannerEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.repository.HomeMainRepository
import com.android.domain.repository.ReservationHistoryInfoRepository
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
    private val homeMainRepository: HomeMainRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _bannerApiState = MutableStateFlow<CommonApiState<List<BannerEntity>>>(
        CommonApiState.Loading
    )
    val bannerApiState: StateFlow<CommonApiState<List<BannerEntity>>> = _bannerApiState

    /**
     * 배너 목록 호출 api
     */
    fun getBannerList(type: String) {
        viewModelScope.launch {
            when (val result = getBannerListUseCase(type)) {
                is ApiResult.Success -> {
                    var bannerList = result.data

                    bannerList = bannerList.map { item ->
                        val updatedImageUrl = getBannerImage(item.imageUrl)
                        item.copy(imageUrl = updatedImageUrl)
                    }

                    _bannerApiState.emit(CommonApiState.Success(bannerList))
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

    /**
     * banner 이미지 api
     */
    private suspend fun getBannerImage(imagePath: String): String {
        return try {
            homeMainRepository.getBannerImage(imagePath)
        } catch (e: Exception) {
            ""
        }
    }
}