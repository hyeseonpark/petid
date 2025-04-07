package com.petid.petid.ui.view.blog

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.R
import com.petid.petid.common.Constants.EXTRA_CONTENT_ID
import com.petid.petid.databinding.ActivityContentDetailBinding
import com.petid.petid.type.ContentCategoryType
import com.petid.petid.ui.state.CommonUIState
import com.petid.petid.ui.view.blog.adapter.MoreContentListAdapter
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.util.collectLatestFlow
import com.petid.petid.util.formatDateFormat
import com.petid.petid.util.showErrorMessage
import com.petid.petid.util.showMessage
import com.petid.petid.util.throttleFirst
import com.petid.petid.util.toSpannedHtml
import com.petid.petid.viewmodel.blog.ContentDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

@AndroidEntryPoint
class ContentDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityContentDetailBinding
    private val viewModel: ContentDetailViewModel by viewModels()

    private lateinit var moreContentListAdapter: MoreContentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // EXTRA_CONTENT_ID 오류 시 activity 종료
        val hospitalId = intent.getIntExtra(EXTRA_CONTENT_ID, -1)
        if (hospitalId == -1) {
            showMessage(getString(R.string.retry_message))
            finish()
            return
        }
    }

    override fun onStart() {
        super.onStart()

        setupToolbar(
            toolbar = findViewById(R.id.toolbar),
            showBackButton = true,
        )

        observeDoLikeState()
        observeGetContentDetailState()
        observeCurrentContentListState()

        initComponent()
    }

    private fun initComponent() {
        with(binding) {
            layoutDefault.visibility = View.GONE
            // 콘텐츠 좋아요
            buttonContentLike
                .clicks()
                .throttleFirst()
                .onEach {
                    when (binding.buttonContentLike.isSelected) {
                        true -> viewModel.cancelContentLike()
                        false -> viewModel.doContentLike()
                    }
                }
                .launchIn(lifecycleScope)

            moreContentListAdapter = MoreContentListAdapter(applicationContext) { item ->
                val target = Intent(this@ContentDetailActivity, ContentDetailActivity::class.java)
                    .putExtra(EXTRA_CONTENT_ID, item.contentId)
                startActivity(target)
            }
            recyclerviewMoreContentList.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = moreContentListAdapter
            }
        }
    }

    /**
     * 컨텐츠 상세 정보
     */
    private fun observeGetContentDetailState() {
        viewModel.contentDetailApiState.collectLatestFlow(this) { result ->
            if (result !is CommonUIState.Loading)
                hideLoading()

            when (result) {
                is CommonUIState.Success -> {
                    val result = result.data

                    with(binding) {
                        textViewContentTitle.text = result.title
                        textViewContentBody.text = result.body.toSpannedHtml(this@ContentDetailActivity)

                        textViewDate.text =
                            formatDateFormat(result.createdAt.split(".")[0].toLong())

                        textViewContentCategory.text = result.category.let {
                            ContentCategoryType.valueOf(it)
                        }.title

                        textViewLike.text =
                            String.format(
                                getString(R.string.content_like_desc),
                                result.likesCount
                            )
                        buttonContentLike.isSelected = result.isLiked

                        if (result.imageUrl.isNullOrEmpty()) {
                            imageViewContentPreview.visibility = View.GONE
                        } else {
                            (R.color.d9).let {
                                Glide.with(getGlobalContext())
                                    .load(result.imageUrl)
                                    .placeholder(it)
                                    .error(it)
                                    .into(imageViewContentPreview)
                            }
                        }

                        layoutDefault.visibility = View.VISIBLE
                    }
                }

                is CommonUIState.Error -> showErrorMessage(result.message.toString())
                is CommonUIState.Loading -> showLoading()
                is CommonUIState.Init -> {}
            }
        }
    }

    /**
     * 콘텐츠 좋아요 하기
     */
    private fun observeDoLikeState() {
        viewModel.doLikeApiResult.collectLatestFlow(this) { result ->
            if (result !is CommonUIState.Loading)
                hideLoading()

            when (result) {
                is CommonUIState.Success -> {
                    val resultData = result.data

                    binding.buttonContentLike.isSelected = !binding.buttonContentLike.isSelected
                    binding.textViewLike.text =
                        String.format(
                            getString(R.string.content_like_desc),
                            resultData.likeCount
                        )

                }

                is CommonUIState.Error -> showErrorMessage(result.message.toString())
                is CommonUIState.Loading -> showLoading()
                is CommonUIState.Init -> {}
            }
        }
    }
    /**
     * 모든 콘텐츠 리스트 조회
     */
    private fun observeCurrentContentListState() {
        viewModel.allContentListApiState.collectLatestFlow(this) { result ->
            if (result !is CommonUIState.Loading)
                hideLoading()

            when (result) {
                is CommonUIState.Success -> {
                    val allContentList = result.data

                    if (allContentList.isNotEmpty()) {
                        val filteredContentList = allContentList
                            .filter { item -> item.contentId != viewModel.contentId }
                            .shuffled() // 순서 섞기
                            .take(3) // 아이템 3개만 가져오기

                        moreContentListAdapter.submitList(filteredContentList)
                    }
                }

                is CommonUIState.Error -> showErrorMessage(result.message.toString())
                is CommonUIState.Loading -> showLoading()
                is CommonUIState.Init -> {}
            }
        }
    }
}
