package com.android.petid.viewmodel.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.BannerEntity
import com.android.domain.entity.MemberInfoEntity
import com.android.domain.entity.PetDetailsEntity
import com.android.domain.repository.HomeMainRepository
import com.android.domain.repository.MyInfoRepository
import com.android.domain.repository.PetInfoRepository
import com.android.domain.usecase.main.GetBannerListUseCase
import com.android.domain.util.ApiResult
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeMainVIewModel @Inject constructor(
    private val getBannerListUseCase: GetBannerListUseCase,
    private val homeMainRepository: HomeMainRepository,
    private val myInfoRepository: MyInfoRepository,
    private val petInfoRepository: PetInfoRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _bannerApiState = MutableStateFlow<CommonApiState<List<BannerEntity>>>(
        CommonApiState.Init
    )
    val bannerApiState = _bannerApiState.asStateFlow()

    /**
     * 배너 목록 호출 api
     */
    fun getBannerList(type: String) {
        viewModelScope.launch {
            when (val result = getBannerListUseCase(type)) {
                is ApiResult.Success -> {
                    var bannerList = result.data

                    var updatedBannerList = bannerList.map { item ->
                        val updatedImageUrl = getBannerImage(item.imageUrl)
                        item.copy(imageUrl = updatedImageUrl)
                    }

                    _bannerApiState.emit(CommonApiState.Success(updatedBannerList))
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

    /* member 정보 가져오기 result*/
    private val _getMemberInfoResult = MutableStateFlow<CommonApiState<MemberInfoEntity>>(
        CommonApiState.Init
    )
    val getMemberInfoResult = _getMemberInfoResult.asStateFlow()

    /**
     * member 정보 가져오기
     */
    fun getMemberInfo() {
        viewModelScope.launch {
            when (val result = myInfoRepository.getMemberInfo()) {
                is ApiResult.Success -> {
                    val memberInfo = result.data
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

    private val _getPetDetailsResult = MutableStateFlow<CommonApiState<PetDetailsEntity>>(
        CommonApiState.Init
    )
    val getPetDetailsResult = _getPetDetailsResult.asStateFlow()

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

    /* bannerScrollPosition 변수 */
    private val _bannerScrollPosition = MutableStateFlow(0)
    val bannerScrollPosition = _bannerScrollPosition.asStateFlow()

    private var autoScrollJob: Job? = null

    /**
     * start banner auto scroll
     */
    fun startAutoScroll(intervalMillis: Long = 3000L) {
        if (autoScrollJob?.isActive == true) return // 이미 실행 중인 작업이 있으면 실행하지 않음

        autoScrollJob = viewModelScope.launch {
            while (true) {
                delay(intervalMillis) // 스크롤 간격
                _bannerScrollPosition.value += 1
            }
        }

    }

    fun stopAutoScroll() {
        autoScrollJob?.cancel() // 코루틴 작업 취소
        autoScrollJob = null
    }

    fun updateCurrentPosition(position: Int) {
        _bannerScrollPosition.value = position
    }
}