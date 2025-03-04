package com.petid.petid.viewmodel.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.domain.entity.CommonInfo
import com.petid.domain.entity.ContentEntity
import com.petid.domain.repository.BlogMainRepository
import com.petid.domain.repository.ContentDetailRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.type.ContentCategoryType
import com.petid.petid.ui.state.CommonUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class CommonInfoViewModel @Inject constructor(
    private val blogMainRepository: BlogMainRepository,
    private val contentDetailRepository: ContentDetailRepository,
): ViewModel() {
    var contentId by Delegates.notNull<Int>()
    lateinit var categoryType: ContentCategoryType

    // content api 결과값
    private val _commonInfoListApiState = MutableStateFlow<CommonUIState<List<CommonInfo>>>(
        CommonUIState.Init
    )
    val commonInfoListApiState = _commonInfoListApiState.asStateFlow()

    // content api 결과값
    private val _contentDetailApiState = MutableStateFlow<CommonUIState<ContentEntity>>(
        CommonUIState.Init
    )
    val contentDetailApiState = _contentDetailApiState.asStateFlow()

    /**
     * 콘텐츠 목록 가져오기
     */
    fun getCommonInfoList() {
        viewModelScope.launch {
            _commonInfoListApiState.emit(CommonUIState.Loading)
            val state = when (val result = blogMainRepository.getCommonInfoList(categoryType.toString())) {
                is ApiResult.Success -> CommonUIState.Success(result.data)
                is ApiResult.HttpError -> CommonUIState.Error(result.error.error)
                is ApiResult.Error -> CommonUIState.Error(result.errorMessage)
            }
            _commonInfoListApiState.emit(state)
        }
    }

    /**
     * 콘텐츠 내용 가져오기
     */
    fun getContentDetail() {
        viewModelScope.launch {
            _contentDetailApiState.emit(CommonUIState.Loading)
            val state = when (val result = contentDetailRepository.getContentDetail(contentId)) {
                is ApiResult.Success -> CommonUIState.Success(result.data)
                is ApiResult.HttpError -> CommonUIState.Error(result.error.error)
                is ApiResult.Error -> CommonUIState.Error(result.errorMessage)
            }
            _contentDetailApiState.emit(state)
        }
    }
}