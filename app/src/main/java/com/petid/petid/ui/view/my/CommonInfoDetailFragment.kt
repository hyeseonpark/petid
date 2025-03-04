package com.petid.petid.ui.view.my

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.petid.petid.R
import com.petid.petid.databinding.FragmentCommonInfoDetailBinding
import com.petid.petid.ui.state.CommonUIState
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.util.formatDateFormat
import com.petid.petid.util.showErrorMessage
import com.petid.petid.viewmodel.my.CommonInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommonInfoDetailFragment
    : BaseFragment<FragmentCommonInfoDetailBinding>(FragmentCommonInfoDetailBinding::inflate) {

        private val viewModel: CommonInfoViewModel by activityViewModels()
        val args: CommonInfoDetailFragmentArgs by navArgs()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentCommonInfoDetailBinding.inflate(inflater)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            setupToolbar(
                toolbar = view.findViewById(R.id.toolbar),
                showBackButton = true,
            )

            initUI()
        }

        private fun initUI() {
            observeGetContentDetailState()
            with(viewModel) {
                contentId = args.contentId
                getContentDetail()
            }
        }


    /**
     * 컨텐츠 상세 정보
     */
    private fun observeGetContentDetailState() {
        lifecycleScope.launch {
            viewModel.contentDetailApiState.collectLatest { result ->
                if (result !is CommonUIState.Loading)
                    hideLoading()

                when (result) {
                    is CommonUIState.Success -> {
                        val result = result.data

                        with(binding) {
                            textViewContentTitle.text = result.title
                            textViewContentBody.text =
                                Html.fromHtml(result.body, Html.FROM_HTML_MODE_LEGACY)

                            textViewDate.text =
                                formatDateFormat(result.createdAt.split(".")[0].toLong())

                        }
                    }

                    is CommonUIState.Error -> showErrorMessage(result.message.toString())
                    is CommonUIState.Loading -> showLoading()
                    is CommonUIState.Init -> {}
                }
            }
        }
    }
}