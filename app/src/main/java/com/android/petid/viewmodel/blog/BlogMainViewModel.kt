package com.android.petid.viewmodel.blog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.ContentEntity
import com.android.domain.entity.ContentLikeEntity
import com.android.domain.entity.LocationEntity
import com.android.domain.repository.BlogMainRepository
import com.android.domain.util.ApiResult
import com.android.petid.enum.ContentCategoryType
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.state.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BlogMainViewModel @Inject constructor(
    private val blogMainRepository: BlogMainRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    // content api 결과값
    private val _contentListApiState = MutableStateFlow<CommonApiState<List<ContentEntity>>>(
        CommonApiState.Init
    )
    val contentListApiState = _contentListApiState.asStateFlow()

    // 좋아요 결과
    private val _doLikeApiResult = MutableSharedFlow<CommonApiState<ContentLikeEntity>>()
    val doLikeApiResult: SharedFlow<CommonApiState<ContentLikeEntity>> = _doLikeApiResult

    /**
     * 콘텐츠 목록 가져오기
     */
    fun getContentList(category: ContentCategoryType) {
        viewModelScope.launch {
            _contentListApiState.emit(CommonApiState.Loading)
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

                    CommonApiState.Success(contentList)
                }
                is ApiResult.HttpError -> {
                    CommonApiState.Error(result.error.error)
                }
                is ApiResult.Error -> {
                    CommonApiState.Error(result.errorMessage)
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
            _doLikeApiResult.emit(CommonApiState.Loading)
            val state = when (val result = blogMainRepository.doContentLike(contentId)) {
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
            _doLikeApiResult.emit(state)
        }
    }

    /**
     * 콘텐츠 좋아요 취소하기
     */
    fun cancelContentLike(contentId: Int) {
        viewModelScope.launch {
            _doLikeApiResult.emit(CommonApiState.Loading)
            val state = when (val result = blogMainRepository.cancelContentLike(contentId)) {
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
            _doLikeApiResult.emit(state)
        }
    }
}