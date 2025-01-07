package com.petid.petid.viewmodel.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.data.repository.local.DBResult
import com.petid.data.repository.local.NotificationRepository
import com.petid.domain.entity.BannerEntity
import com.petid.domain.entity.MemberInfoEntity
import com.petid.domain.entity.PetDetailsEntity
import com.petid.domain.repository.HomeMainRepository
import com.petid.domain.repository.MyInfoRepository
import com.petid.domain.repository.PetInfoRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeMainViewModel @Inject constructor(
    private val homeMainRepository: HomeMainRepository,
    private val myInfoRepository: MyInfoRepository,
    private val petInfoRepository: PetInfoRepository,
    private val notificationRepository: NotificationRepository,
): ViewModel() {

    /* 읽지 않은 notification 여부 상태값*/
    private val _unchekedNotifcationState = MutableStateFlow<CommonApiState<Boolean>>(
        CommonApiState.Init
    )
    val unchekedNotifcationState = _unchekedNotifcationState.asStateFlow()

    /**
     * 읽지 않은 notification 여부
     */
    fun hasUncheckedNotification() {
        viewModelScope.launch {
            val state = when (val result = notificationRepository.hasUncheckedNotification()) {
                is DBResult.Success -> {
                    CommonApiState.Success(result.data)
                }
                is DBResult.Error -> {
                    CommonApiState.Error(result.exception.message)
                }
            }
            _unchekedNotifcationState.emit(state)
        }
    }

    /* banner 상태값 */
    private val _bannerApiState = MutableStateFlow<CommonApiState<List<BannerEntity>>>(
        CommonApiState.Init
    )
    val bannerApiState = _bannerApiState.asStateFlow()

    /**
     * 배너 목록 호출 api
     */
    fun getBannerList(type: String) {
        viewModelScope.launch {
            if(_bannerApiState.value != CommonApiState.Init) return@launch

            _bannerApiState.emit(CommonApiState.Loading)
            val state = when (val result = homeMainRepository.getBannerList(type)) {
                is ApiResult.Success -> {
                    val bannerList = result.data

                    val updatedBannerList = bannerList.map { item ->
                        val updatedImageUrl = getBannerImage(item.imageUrl)
                        item.copy(imageUrl = updatedImageUrl)
                    }

                    CommonApiState.Success(updatedBannerList)
                }
                is ApiResult.HttpError ->{
                    CommonApiState.Error(result.error.error)
                }
                is ApiResult.Error -> {
                    CommonApiState.Error(result.errorMessage)
                }
            }
            _bannerApiState.emit(state)
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
            _getMemberInfoResult.emit(CommonApiState.Loading)
            val state = when (val result = myInfoRepository.getMemberInfo()) {
                is ApiResult.Success -> {
                    val memberInfo = result.data
                    CommonApiState.Success(memberInfo)
                }
                is ApiResult.HttpError -> {
                    CommonApiState.Error(result.error.error)
                }
                is ApiResult.Error -> {
                    CommonApiState.Error(result.errorMessage)
                }
            }
            _getMemberInfoResult.emit(state)
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
            _getPetDetailsResult.emit(CommonApiState.Loading)
            val state = when (val result = petInfoRepository.getPetDetails(petId)) {
                is ApiResult.Success -> {
                    var petDetails = result.data
                    getPetImageUrl(petDetails.petImages.first().imagePath)
                    CommonApiState.Success(petDetails)
                }
                is ApiResult.HttpError -> {
                    CommonApiState.Error(result.error.error)
                }
                is ApiResult.Error -> {
                    CommonApiState.Error(result.errorMessage)
                }
            }
            _getPetDetailsResult.emit(state)
        }
    }

    /* 펫 이미지 가져오기 결과*/
    private val _getPetImageUrlResult = MutableStateFlow<CommonApiState<String>>(
        CommonApiState.Init
    )
    val getPetImageUrlResult = _getPetImageUrlResult.asStateFlow()

    /**
     * 펫 이미지 S3 주소 가져오기
     */
    private fun getPetImageUrl(filePath: String) {
        viewModelScope.launch {
            _getPetImageUrlResult.emit(CommonApiState.Loading)
            val state = when (val result = petInfoRepository.getPetImageUrl(filePath)) {
                is ApiResult.Success -> {
                    CommonApiState.Success(result.data)
                }
                is ApiResult.HttpError -> {
                    CommonApiState.Error(result.error.error)
                }
                is ApiResult.Error -> {
                    CommonApiState.Error(result.errorMessage)
                }
            }
            _getPetImageUrlResult.emit(state)
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