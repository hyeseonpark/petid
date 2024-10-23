package com.android.petid.viewmodel.blog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.ContentEntity
import com.android.domain.usecase.content.DoContentLikeUseCase
import com.android.domain.usecase.content.GetContentDetailUseCase
import com.android.domain.util.ApiResult
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class ContentDetailViewModel @Inject constructor(
    private val getContentDetailUseCase: GetContentDetailUseCase,
    private val doContentLikeUseCase: DoContentLikeUseCase,
//    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    var contentId by Delegates.notNull<Int>()

    // content api 결과값
    private val _contentDetailApiState = MutableStateFlow<CommonApiState<ContentEntity>>(
        CommonApiState.Loading
    )
    val contentDetailApiState: StateFlow<CommonApiState<ContentEntity>> = _contentDetailApiState


    // 좋아요 결과
    private val _doLikeApiResult = MutableSharedFlow<CommonApiState<Unit>>()
    val doLikeApiResult: SharedFlow<CommonApiState<Unit>> = _doLikeApiResult

    /**
     * 콘텐츠 내용 가져오기
     */
    fun getContentDetail() {
        viewModelScope.launch {
            when (val result = getContentDetailUseCase(contentId)) {
                is ApiResult.Success -> {
                    _contentDetailApiState.emit(CommonApiState.Success(result.data))
                }
                is ApiResult.HttpError -> {
                    _contentDetailApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _contentDetailApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
        }
    }

    /**
     * 콘텐츠 좋아요 하기
     */
    fun doContentLike() {
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