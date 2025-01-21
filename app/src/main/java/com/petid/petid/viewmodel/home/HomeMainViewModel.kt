package com.petid.petid.viewmodel.home

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
import com.petid.petid.common.Constants.BANNER_TYPE_CONTENT
import com.petid.petid.common.Constants.BANNER_TYPE_MAIN
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
    private val _mainBannerApiState = MutableStateFlow<CommonApiState<List<BannerEntity>>>(
        CommonApiState.Init
    )
    val mainBannerApiState = _mainBannerApiState.asStateFlow()

    /* 상태값: Content Banner */
    private val _contentBannerApiState = MutableStateFlow<CommonApiState<List<BannerEntity>>>(
        CommonApiState.Init
    )
    val contentBannerApiState = _contentBannerApiState.asStateFlow()

    /**
     * Main 배너 데이터 호출
     */
    fun getMainBannerList() {
        viewModelScope.launch {
            fetchBannerList(BANNER_TYPE_MAIN, _mainBannerApiState)
        }
    }

    /**
     * Content 배너 데이터 호출
     */
    fun getContentBannerList() {
        viewModelScope.launch {
            fetchBannerList(BANNER_TYPE_CONTENT, _contentBannerApiState)
        }
    }


    /**
     * 배너 목록 호출 api
     */
    private fun fetchBannerList(
        type: String,
        bannerApiState: MutableStateFlow<CommonApiState<List<BannerEntity>>>
    ) {
        viewModelScope.launch {
            if(bannerApiState.value != CommonApiState.Init) return@launch

            bannerApiState.emit(CommonApiState.Loading)
            val state = when (val result = homeMainRepository.getBannerList(type)) {
                is ApiResult.Success -> {
                    result.data
                        .filter { it.status == "active" }
                        .map { item ->
                            val updatedImageUrl = getBannerImage(item.imageUrl)
                            item.copy(imageUrl = updatedImageUrl)
                        }
                        .let { CommonApiState.Success(it) }
                }
                is ApiResult.HttpError -> CommonApiState.Error(result.error.error)
                is ApiResult.Error -> CommonApiState.Error(result.errorMessage)
            }
            bannerApiState.emit(state)
        }
    }

    /**
     * banner 이미지 api
     */
    private suspend fun getBannerImage(imagePath: String): String {
        return when (val result = homeMainRepository.getBannerImage(imagePath)) {
            is ApiResult.Success -> result.data
            is ApiResult.HttpError -> ""
            is ApiResult.Error -> ""
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

    /* mainBannerScrollPosition 변수 */
    private val _mainBannerScrollPosition = MutableStateFlow(0)
    val mainBannerScrollPosition = _mainBannerScrollPosition.asStateFlow()
    private var mainAutoScrollJob: Job? = null

    fun startMainAutoScroll(intervalMillis: Long = 3000L) {
        if (mainAutoScrollJob?.isActive == true) return // 이미 실행 중인 작업이 있으면 실행하지 않음

        mainAutoScrollJob = viewModelScope.launch {
            while (true) {
                delay(intervalMillis) // 스크롤 간격
                _mainBannerScrollPosition.value += 1
            }
        }
    }

    fun stopMainAutoScroll() {
        mainAutoScrollJob?.cancel() // 코루틴 작업 취소
        mainAutoScrollJob = null
    }

    fun updateMainCurrentPosition(position: Int) {
        _mainBannerScrollPosition.value = position
    }

    /* contentBannerScrollPosition 변수 */
    private val _contentBannerScrollPosition = MutableStateFlow(0)
    val contentBannerScrollPosition = _contentBannerScrollPosition.asStateFlow()
    private var contentAutoScrollJob: Job? = null

    fun startContentAutoScroll(intervalMillis: Long = 3000L) {
        if (contentAutoScrollJob?.isActive == true) return // 이미 실행 중인 작업이 있으면 실행하지 않음

        contentAutoScrollJob = viewModelScope.launch {
            while (true) {
                delay(intervalMillis) // 스크롤 간격
                _contentBannerScrollPosition.value += 1
            }
        }
    }

    fun stopContentAutoScroll() {
        contentAutoScrollJob?.cancel() // 코루틴 작업 취소
        contentAutoScrollJob = null
    }

    fun updateContentCurrentPosition(position: Int) {
        _contentBannerScrollPosition.value = position
    }
}