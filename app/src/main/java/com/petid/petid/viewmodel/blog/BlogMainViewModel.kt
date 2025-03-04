package com.petid.petid.viewmodel.blog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.domain.entity.ContentEntity
import com.petid.domain.entity.ContentLikeEntity
import com.petid.domain.repository.BlogMainRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.type.ContentCategoryType
import com.petid.petid.ui.state.CommonUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BlogMainViewModel @Inject constructor(
    private val blogMainRepository: BlogMainRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    // content api 결과값
    private val _contentListApiState = MutableStateFlow<CommonUIState<List<ContentEntity>>>(
        CommonUIState.Init
    )
    val contentListApiState = _contentListApiState.asStateFlow()

    // 좋아요 결과
    private val _doLikeApiResult = MutableSharedFlow<CommonUIState<ContentLikeEntity>>()
    val doLikeApiResult: SharedFlow<CommonUIState<ContentLikeEntity>> = _doLikeApiResult

    /**
     * 콘텐츠 목록 가져오기
     */
    fun getContentList(category: ContentCategoryType) {
        viewModelScope.launch {
            _contentListApiState.emit(CommonUIState.Loading)
            val state = when (val result = blogMainRepository.getContentList(category.name)) {
                is ApiResult.Success -> {
                    var contentList = result.data

                    contentList = contentList.map { item ->
                        val updatedImageUrl = when (item.imageUrl != null) {
                            true -> getContentImage(item.imageUrl!!)
                            false -> ""
                        }
                        item.copy(imageUrl = updatedImageUrl)
                    }

                    CommonUIState.Success(contentList)
                }
                is ApiResult.HttpError -> {
                    CommonUIState.Error(result.error.error)
                }
                is ApiResult.Error -> {
                    CommonUIState.Error(result.errorMessage)
                }
            }
            _contentListApiState.emit(state)
        }
    }

    /**
     * 컨텐츠 이미지 가져오기
     */
    private suspend fun getContentImage(filePath: String): String {
        return try {
            blogMainRepository.getContentImage(filePath)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 콘텐츠 좋아요 하기
     */
    fun doContentLike(contentId: Int) {
        viewModelScope.launch {
            _doLikeApiResult.emit(CommonUIState.Loading)
            val state = when (val result = blogMainRepository.doContentLike(contentId)) {
                is ApiResult.Success -> {
                    CommonUIState.Success(result.data)
                }
                is ApiResult.HttpError -> {
                    CommonUIState.Error(result.error.error)
                }
                is ApiResult.Error -> {
                    CommonUIState.Error(result.errorMessage)
                }
            }
            _doLikeApiResult.emit(state)
        }
    }

    /**
     * 콘텐츠 좋아요 취소하기
     */
    fun cancelContentLike(contentId: Int) {
        viewModelScope.launch {
            _doLikeApiResult.emit(CommonUIState.Loading)
            val state = when (val result = blogMainRepository.cancelContentLike(contentId)) {
                is ApiResult.Success -> {
                    CommonUIState.Success(result.data)
                }
                is ApiResult.HttpError -> {
                    CommonUIState.Error(result.error.error)
                }
                is ApiResult.Error -> {
                    CommonUIState.Error(result.errorMessage)
                }
            }
            _doLikeApiResult.emit(state)
        }
    }
}