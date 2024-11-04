package com.android.petid.ui.view.blog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.android.data.dto.response.ContentResponse
import com.android.domain.entity.ContentEntity
import com.android.petid.databinding.FragmentBlogMainBinding
import com.android.petid.enum.ContentCategoryType
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.blog.adapter.ContentListAdapter
import com.android.petid.ui.view.hospital.HospitalDetailActivity
import com.android.petid.ui.view.hospital.adapter.HospitalListAdapter
import com.android.petid.viewmodel.blog.BlogMainViewModel
import com.android.petid.viewmodel.hospital.HospitalMainViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BlogMainFragment : Fragment() {
    lateinit var binding: FragmentBlogMainBinding
    private val viewModel: BlogMainViewModel by activityViewModels()

    private val TAG = "BlogMainFragment"

    lateinit var contentList : List<ContentEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBlogMainBinding.inflate(inflater)

        initComponent()

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeCurrentContentListState()
        observeDoLikeState()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getContentList(ContentCategoryType.RECOMMENDED)
    }

    private fun initComponent() {
        binding.recyclerviewBlogContentList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewBlogContentList.addItemDecoration(
            DividerItemDecoration(context, LinearLayout.VERTICAL)
        )

        binding.tabLayoutBlogMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val category = when (tab?.position) {
                    0 -> ContentCategoryType.RECOMMENDED
                    1 -> ContentCategoryType.ABOUTPET
                    2 -> ContentCategoryType.TIPS
                    3 -> ContentCategoryType.VENUE
                    4 -> ContentCategoryType.SUPPORT
                    else -> ContentCategoryType.ALL
                }
                viewModel.getContentList(category)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
        })
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
                                val contentListAdapter =
                                    ContentListAdapter(contentList, requireActivity(), {
                                        viewModel.doContentLike(it)
                                    }) { contentId ->
                                        val intent = Intent(activity, ContentDetailActivity::class.java)
                                            .putExtra("contentId", contentId)
                                        startActivity(intent)
                                    }

                                binding.recyclerviewBlogContentList.adapter = contentListAdapter

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
                }
            }
        }
    }

    /**
     * 데이터 유무에 따른 화면 전환
     */
    private fun visibleLayoutDataAvailable(boolean: Boolean) {
        when(boolean) {
            true -> {
                if (binding.layoutDataAvailable.visibility != View.VISIBLE) {
                    binding.layoutDataAvailable.visibility = View.VISIBLE
                    binding.layoutNoData.visibility = View.GONE
                }
            }
            false -> {
                if (binding.layoutNoData.visibility != View.VISIBLE) {
                    binding.layoutNoData.visibility = View.VISIBLE
                    binding.layoutDataAvailable.visibility = View.GONE
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

                        var updatedContent = contentList.find { it.contentId == result.contentId }
                        updatedContent?.isLiked = true
                        updatedContent?.likesCount = result.likeCount

                        binding.recyclerviewBlogContentList.adapter?.notifyDataSetChanged()
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                }
            }
        }
    }
}