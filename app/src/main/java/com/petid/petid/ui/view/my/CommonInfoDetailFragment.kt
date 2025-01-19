package com.petid.petid.ui.view.my

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.R
import com.petid.petid.databinding.FragmentCommonInfoDetailBinding
import com.petid.petid.databinding.FragmentCommonInfoListBinding
import com.petid.petid.type.ContentCategoryType
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.util.formatDateFormat
import com.petid.petid.util.showErrorMessage
import com.petid.petid.viewmodel.my.CommonInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.URL

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
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> {
                        val result = result.data

                        with(binding) {
                            textViewContentTitle.text = result.title
                            textViewContentBody.text =
                                Html.fromHtml(result.body, Html.FROM_HTML_MODE_LEGACY)

                            textViewDate.text =
                                formatDateFormat(result.createdAt.split(".")[0].toLong())

                        }
                    }

                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
                }
            }
        }
    }
}