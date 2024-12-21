package com.android.petid.ui.view.blog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.domain.entity.ContentEntity
import com.android.petid.R
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentBlogMainBinding
import com.android.petid.enum.ContentCategoryType
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.blog.adapter.ContentListAdapter
import com.android.petid.viewmodel.blog.BlogMainViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BlogMainFragment : BaseFragment<FragmentBlogMainBinding>(FragmentBlogMainBinding::inflate) {
    private val viewModel: BlogMainViewModel by activityViewModels()

    private val TAG = "BlogMainFragment"

    private lateinit var contentList : List<ContentEntity>
    private lateinit var contentListAdapter : ContentListAdapter
    var currentCategory = ContentCategoryType.ALL

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlogMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            title = getString(R.string.main_blog_title)
        )

        initComponent()
        observeCurrentContentListState()
        observeDoLikeState()
    }

    override fun onResume() {
        super.onResume()
        // 마지막으로 접속한 탭의 화면 업데이트
        viewModel.getContentList(currentCategory)
    }

    private fun initComponent() {
        with(binding) {
            contentListAdapter =
                ContentListAdapter(requireActivity(),
                    {viewModel.doContentLike(it)},
                    {viewModel.cancelContentLike(it)}) { contentId ->
                    val intent = Intent(activity, ContentDetailActivity::class.java)
                        .putExtra("contentId", contentId)
                    startActivity(intent)
                }
            recyclerviewBlogContentList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
                adapter = contentListAdapter
            }

            tabLayoutBlogMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    currentCategory = when (tab?.position) {
                        0 -> ContentCategoryType.ALL
                        1 -> ContentCategoryType.ABOUTPET
                        2 -> ContentCategoryType.TIPS
                        3 -> ContentCategoryType.VENUE
                        4 -> ContentCategoryType.SUPPORT
                        else -> ContentCategoryType.ALL
                    }
                    viewModel.getContentList(currentCategory)
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                }

                override fun onTabReselected(p0: TabLayout.Tab?) {
                }
            })
        }
    }

    /**
     * 콘텐츠 리스트 조회
     */
    private fun observeCurrentContentListState() {
        lifecycleScope.launch {
            viewModel.contentListApiState.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        contentList = result.data

                        when(contentList.isNotEmpty()) {
                            true -> {
                                contentListAdapter.submitList(contentList)
                                visibleLayoutDataAvailable(true)
                            }
                            false -> visibleLayoutDataAvailable(false)
                        }
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                        visibleLayoutDataAvailable(false)
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                        visibleLayoutDataAvailable(false)
                    }
                    is CommonApiState.Init -> visibleLayoutDataAvailable(false)
                }
            }
        }
    }

    /**
     * 데이터 유무에 따른 화면 전환
     */
    private fun visibleLayoutDataAvailable(boolean: Boolean) {
        with(binding) {
            when(boolean) {
                true -> {
                    if (layoutDataAvailable.visibility != View.VISIBLE) {
                        layoutDataAvailable.visibility = View.VISIBLE
                        layoutNoData.visibility = View.GONE
                    }
                }
                false -> {
                    if (layoutNoData.visibility != View.VISIBLE) {
                        layoutNoData.visibility = View.VISIBLE
                        layoutDataAvailable.visibility = View.GONE
                    }
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
                        val result = result.data

                        val index = contentList.indexOfFirst { it.contentId == result.contentId }

                        if (index != -1) {
                            val newList = contentList.toMutableList()
                            newList[index] = newList[index].copy(
                                isLiked = !newList[index].isLiked,
                                likesCount = result.likeCount
                            )
                            contentList = newList
                            contentListAdapter.submitList(newList)
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