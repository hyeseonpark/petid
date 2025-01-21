package com.petid.petid.ui.view.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.petid.domain.entity.CommonInfo
import com.petid.petid.R
import com.petid.petid.databinding.FragmentCommonInfoListBinding
import com.petid.petid.type.ContentCategoryType
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.ui.view.my.adapter.CommonInfoListAdapter
import com.petid.petid.util.showErrorMessage
import com.petid.petid.viewmodel.my.CommonInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommonInfoListFragment
    : BaseFragment<FragmentCommonInfoListBinding>(FragmentCommonInfoListBinding::inflate) {
        private val viewModel: CommonInfoViewModel by activityViewModels()
        private lateinit var commonInfoList : List<CommonInfo>

        private lateinit var commonInfoListAdapter : CommonInfoListAdapter

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentCommonInfoListBinding.inflate(inflater)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            setupToolbar(
                toolbar = view.findViewById(R.id.toolbar),
                showBackButton = true,
                onBackClick = { requireActivity().finish() },
                title = viewModel.categoryType.title,
            )

            initUI()
            viewModel.getCommonInfoList()
            observeCurrentCommonInfoListState()
        }

        private fun initUI() {

            // adapter 초기화
            commonInfoListAdapter =
                CommonInfoListAdapter(requireActivity()) { id ->
                    val action = CommonInfoListFragmentDirections
                        .actionCommonInfoListFragmentToCommonInfoDetailFragment(id)
                    findNavController().navigate(action)
                }

            with(binding.recyclerviewCommonInfoList) {
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(
                    DividerItemDecoration(requireContext(), LinearLayout.VERTICAL)
                )

                adapter = commonInfoListAdapter
            }
        }

        /**
         * 콘텐츠 리스트 조회
         */
        private fun observeCurrentCommonInfoListState() {
            lifecycleScope.launch {
                viewModel.commonInfoListApiState.collectLatest { result ->
                    if (result !is CommonApiState.Loading)
                        hideLoading()

                    when (result) {
                        is CommonApiState.Success -> {
                            commonInfoList = result.data
                            commonInfoListAdapter.submitList(commonInfoList)
                        }
                        is CommonApiState.Error -> showErrorMessage(result.message.toString())
                        is CommonApiState.Loading -> showLoading()
                        is CommonApiState.Init -> {}
                    }
                }
            }
        }
}