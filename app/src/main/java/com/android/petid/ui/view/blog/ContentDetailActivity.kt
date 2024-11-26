package com.android.petid.ui.view.blog

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.domain.entity.ContentEntity
import com.android.domain.entity.HospitalEntity
import com.android.petid.R
import com.android.petid.databinding.ActivityContentDetailBinding
import com.android.petid.enum.ContentCategoryType
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.blog.adapter.MoreContentListAdapter
import com.android.petid.ui.view.hospital.HospitalActivity
import com.android.petid.viewmodel.blog.ContentDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class ContentDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentDetailBinding
    private val viewModel: ContentDetailViewModel by viewModels()

    private val TAG = "ContentDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentDetailBinding.inflate(layoutInflater)

        observeDoLikeState()
        observeGetContentDetailState()

        initComponent()

        setContentView(binding.root)
    }

    private fun initComponent() {
        viewModel.contentId = intent.getIntExtra("contentId", -1)
        viewModel.getContentDetail()

        // 콘텐츠 좋아요
        binding.buttonContentLike.setOnClickListener {
            when(binding.buttonContentLike.isSelected) {
                true -> viewModel.cancelContentLike()
                false -> viewModel.doContentLike()
            }
        }

        val moreContentListAdapter = MoreContentListAdapter(applicationContext) { item ->
            val intent = Intent(this, ContentDetailActivity::class.java)
                .putExtra("contentId", item.contentId)
            startActivity(intent)
        }
        binding.recyclerviewMoreContentList.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = moreContentListAdapter
        }

        // TODO temp
        val contentList: List<ContentEntity> = listOf(
            ContentEntity(
                contentId = 5,
                title = "Pet-Friendly Cafes in the City",
                body = "Looking for places to hang out with your pets? Che",
                category = "TIPS",
                imageUrl = "petImg/darthVader.jpg",
                createdAt = "1695038400",
                updatedAt = "1729063206",
                likesCount = 1,
                authorId = 4,
                isLiked = false
            ),
            ContentEntity(
                contentId = 3,
                title = "Pet Care Tips for New Owners",
                body = "<p>Here are some tips to get you started.asdfasdf<",
                category = "TIPS",
                imageUrl = "petImg/darthVader.jpg",
                createdAt = "1695288600",
                updatedAt = "1729063206",
                likesCount = 1,
                authorId = 3,
                isLiked = false
            ),
            ContentEntity(
                contentId = 1,
                title = "How to Groom Your Dogaaaa",
                body = "<p>Learn the basics of grooming your dog with thes",
                category = "TIPS",
                imageUrl = "petImg/darthVader.jpg",
                createdAt = "1695650400",
                updatedAt = "1729063206",
                likesCount = 12,
                authorId = 1,
                isLiked = false
            )
        )

        moreContentListAdapter.submitList(contentList)
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
                            textViewContentBody.text =
                                Html.fromHtml(result.body, Html.FROM_HTML_MODE_LEGACY)

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

                            /*contentItem.imageUrl?.let {
                                Glide.with(mContext)
                                    .load(it)
                                    .into(imageViewContentPreview)
                            }*/
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
}