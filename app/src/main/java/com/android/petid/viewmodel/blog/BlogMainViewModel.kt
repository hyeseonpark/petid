package com.android.petid.viewmodel.blog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.ContentEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.usecase.content.DoContentLikeUseCase
import com.android.domain.usecase.content.GetContentListUseCase
import com.android.domain.util.ApiResult
import com.android.petid.enum.ContentCategoryType
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.state.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BlogMainViewModel @Inject constructor(
    private val getContentListUseCase: GetContentListUseCase,
    private val doContentLikeUseCase: DoContentLikeUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    // content api 결과값
    private val _contentListApiState = MutableStateFlow<CommonApiState<List<ContentEntity>>>(
        CommonApiState.Loading
    )
    val contentListApiState: StateFlow<CommonApiState<List<ContentEntity>>> = _contentListApiState

    // 좋아요 결과
    private val _doLikeApiResult = MutableSharedFlow<CommonApiState<Unit>>()
    val doLikeApiResult: SharedFlow<CommonApiState<Unit>> = _doLikeApiResult

    /**
     * 콘텐츠 목록 가져오기
     */
    fun getContentList(category: ContentCategoryType) {
        viewModelScope.launch {
            when (val result = getContentListUseCase(category.name)) {
                is ApiResult.Success -> {
                    _contentListApiState.emit(CommonApiState.Success(result.data))
                }
                is ApiResult.HttpError -> {
                    _contentListApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _contentListApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }

    /**
     * 콘텐츠 좋아요 하기
     */
    fun doContentLike(contentId: Int) {
        viewModelScope.launch {
            when (val result = doContentLikeUseCase(contentId)) {
                is ApiResult.Success -> {
                    _doLikeApiResult.emit(CommonApiState.Success(result.data))
                }
                is ApiResult.HttpError -> {
                    _doLikeApiResult.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _doLikeApiResult.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }
}