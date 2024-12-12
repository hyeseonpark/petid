package com.android.petid.viewmodel.blog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.domain.entity.ContentEntity
import com.android.domain.entity.ContentLikeEntity
import com.android.domain.repository.BlogMainRepository
import com.android.domain.repository.ContentDetailRepository
import com.android.domain.util.ApiResult
import com.android.petid.enum.ContentCategoryType
import com.android.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale.Category
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class ContentDetailViewModel @Inject constructor(
    private val blogMainRepository: BlogMainRepository,
    private val contentDetailRepository: ContentDetailRepository,
//    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    var contentId by Delegates.notNull<Int>()

    // content api 결과값
    private val _contentDetailApiState = MutableStateFlow<CommonApiState<ContentEntity>>(
        CommonApiState.Init
    )
    val contentDetailApiState = _contentDetailApiState.asStateFlow()


    // get all content list api 결과값
    private val _allContentListApiState = MutableStateFlow<CommonApiState<List<ContentEntity>>>(
        CommonApiState.Init
    )
    val allContentListApiState = _allContentListApiState.asStateFlow()

    // 좋아요 결과
    private val _doLikeApiResult = MutableSharedFlow<CommonApiState<ContentLikeEntity>>()
    val doLikeApiResult: SharedFlow<CommonApiState<ContentLikeEntity>> = _doLikeApiResult

    /**
     * 콘텐츠 내용 가져오기
     */
    fun getContentDetail() {
        viewModelScope.launch {
            val state = when (val result = contentDetailRepository.getContentDetail(contentId)) {
                is ApiResult.Success -> {
                    val contentDetail = result.data
                    val updatedDetail = contentDetail.copy(
                        imageUrl = contentDetail.imageUrl?.let { getContentImage(it) } ?: ""
                    )
                    CommonApiState.Success(updatedDetail)
                }
                is ApiResult.HttpError -> {
                    CommonApiState.Error(result.error.error)
                }
                is ApiResult.Error -> {
                    CommonApiState.Error(result.errorMessage)
                }
            }
            _contentDetailApiState.emit(state)
        }
    }

    /**
     * 콘텐츠 좋아요 하기
     */
    fun doContentLike() {
        viewModelScope.launch {
            when (val result = blogMainRepository.doContentLike(contentId)) {
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

    /**
     * 콘텐츠 좋아요 취소하기
     */
    fun cancelContentLike() {
        viewModelScope.launch {
            when (val result = blogMainRepository.cancelContentLike(contentId)) {
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


    /**
     * 콘텐츠 목록 가져오기
     */
    fun getAllContentList() {
        viewModelScope.launch {
            when (val result = blogMainRepository.getContentList(ContentCategoryType.ALL.name)) {
                is ApiResult.Success -> {
                    var contentList = result.data

                    contentList = contentList.map { item ->
                        val updatedImageUrl = when (item.imageUrl != null) {
                            true -> getContentImage(item.imageUrl!!)
                            false -> ""
                        }
                        item.copy(imageUrl = updatedImageUrl)
                    }

                    _allContentListApiState.emit(CommonApiState.Success(contentList))
                }
                is ApiResult.HttpError -> {
                    _allContentListApiState.emit(CommonApiState.Error(result.error.error))
                }
                is ApiResult.Error -> {
                    _allContentListApiState.emit(CommonApiState.Error(result.errorMessage))
                }
            }
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
}