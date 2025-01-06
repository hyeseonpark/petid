package com.petid.petid.viewmodel.blog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petid.domain.entity.ContentEntity
import com.petid.domain.entity.ContentLikeEntity
import com.petid.domain.repository.BlogMainRepository
import com.petid.domain.repository.ContentDetailRepository
import com.petid.domain.util.ApiResult
import com.petid.petid.type.ContentCategoryType
import com.petid.petid.ui.state.CommonApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
            _contentDetailApiState.emit(CommonApiState.Loading)
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
    fun cancelContentLike() {
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


    /**
     * 콘텐츠 목록 가져오기
     */
    fun getAllContentList() {
        viewModelScope.launch {
            _allContentListApiState.emit(CommonApiState.Loading)
            val state = when (val result = blogMainRepository.getContentList(ContentCategoryType.ALL.name)) {
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
            _allContentListApiState.emit(state)
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