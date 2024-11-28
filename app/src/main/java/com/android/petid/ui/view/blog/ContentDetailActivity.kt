package com.android.petid.ui.view.blog

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.android.domain.entity.ContentEntity
import com.android.domain.entity.HospitalEntity
import com.android.petid.R
import com.android.petid.databinding.ActivityContentDetailBinding
import com.android.petid.enum.ContentCategoryType
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.blog.adapter.MoreContentListAdapter
import com.android.petid.ui.view.common.BaseActivity
import com.android.petid.ui.view.hospital.HospitalActivity
import com.android.petid.viewmodel.blog.ContentDetailViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.net.URL
import java.util.Locale

@AndroidEntryPoint
class ContentDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityContentDetailBinding
    private val viewModel: ContentDetailViewModel by viewModels()

    private val TAG = "ContentDetailActivity"

    private lateinit var moreContentListAdapter : MoreContentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentDetailBinding.inflate(layoutInflater)

        observeDoLikeState()
        observeGetContentDetailState()
        observeCurrentContentListState()

        initComponent()

        setContentView(binding.root)
    }

    private fun initComponent() {
        viewModel.contentId = intent.getIntExtra("contentId", -1)
        viewModel.getContentDetail()
        viewModel.getAllContentList()

        // 콘텐츠 좋아요
        binding.buttonContentLike.setOnClickListener {
            when(binding.buttonContentLike.isSelected) {
                true -> viewModel.cancelContentLike()
                false -> viewModel.doContentLike()
            }
        }

        moreContentListAdapter = MoreContentListAdapter(applicationContext) { item ->
            val intent = Intent(this, ContentDetailActivity::class.java)
                .putExtra("contentId", item.contentId)
            startActivity(intent)
        }
        binding.recyclerviewMoreContentList.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = moreContentListAdapter
        }
    }

    /**
     * 컨텐츠 상세 정보
     */
    private fun observeGetContentDetailState() {
        lifecycleScope.launch {
            viewModel.contentDetailApiState.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        val result = result.data

                        with(binding) {
                            textViewContentTitle.text = result.title

                            textViewContentBody.text  = Html.fromHtml(result.body, Html.FROM_HTML_MODE_LEGACY, { source ->
                                try {
                                    if (source.startsWith("data:")) {
                                        // Base64 이미지 처리
                                        val base64Data = source.substringAfter("base64,")
                                        val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
                                        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                                        val drawable = BitmapDrawable(resources, bitmap)
                                        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                                        drawable
                                    } else {
                                        // 일반 URL 이미지 처리
                                        val inputStream = URL(source).openStream()
                                        val drawable = Drawable.createFromStream(inputStream, null)
                                        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                                        drawable
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    null // 실패 시 null 반환
                                }
                            }, null)

                            textViewDate.text =
                                formatInstantToDateTime(result.createdAt.split(".")[0].toLong())

                            textViewContentCategory.text = when(result.category) {
                                ContentCategoryType.RECOMMENDED.name -> getString(R.string.tab_recommendation_title)
                                ContentCategoryType.TIPS.name -> getString(R.string.tab_tips_title)
                                ContentCategoryType.ABOUTPET.name -> getString(R.string.tab_about_pet_title)
                                ContentCategoryType.VENUE.name -> getString(R.string.tab_venue_title)
                                ContentCategoryType.SUPPORT.name -> getString(R.string.tab_support_title)
                                else -> ""
                            }

                            textViewLike.text =
                                String.format(getString(R.string.content_like_desc), result.likesCount)
                            buttonContentLike.isSelected = result.isLiked

                            if (result.imageUrl.isNullOrEmpty()) {
                                imageViewContentPreview.visibility = View.GONE
                            } else {
                                (R.color.d9).let {
                                    Glide.with(applicationContext)
                                        .load(result.imageUrl)
                                        .placeholder(it)
                                        .error(it)
                                        .into(imageViewContentPreview)
                                }
                            }
                        }
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                    is CommonApiState.Init -> {}
                }
            }
        }
    }

    /**
     * 콘텐츠 좋아요 하기
     */
    private fun observeDoLikeState() {
        lifecycleScope.launch {
            viewModel.doLikeApiResult.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        val resultData = result.data
                        // TODO api result 값 수정 되면 화면 반영
                        binding.buttonContentLike.isSelected = !binding.buttonContentLike.isSelected
                        binding.textViewLike.text =
                            String.format(getString(R.string.content_like_desc), resultData.likeCount)

                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                    is CommonApiState.Init -> {}
                }
            }
        }
    }

    /**
     * date 값 변환
     */
    private fun formatInstantToDateTime(timestamp: Long): String {
        val instant = Instant.ofEpochSecond(timestamp)

        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일(E) HH:mm", Locale.KOREAN)

        return dateTime.format(formatter)
    }


    /**
     * 모든 콘텐츠 리스트 조회
     */
    private fun observeCurrentContentListState() {
        lifecycleScope.launch {
            viewModel.allContentListApiState.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        val allContentList = result.data

                        if(allContentList.isNotEmpty()) {
                            val filteredContentList = allContentList
                                .filter { item -> item.contentId != viewModel.contentId }
                                .shuffled() // 순서 섞기
                                .take(3) // 아이템 3개만 가져오기

                            moreContentListAdapter.submitList(filteredContentList)
                        }
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                    is CommonApiState.Init -> {}
                }
            }
        }
    }
}