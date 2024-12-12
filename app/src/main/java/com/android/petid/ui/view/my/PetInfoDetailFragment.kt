package com.android.petid.ui.view.my

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.common.Constants
import com.android.petid.common.GlobalApplication.Companion.getPreferencesControl
import com.android.petid.databinding.FragmentPetInfoDetailBinding
import com.android.petid.ui.component.CustomDialogCommon
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.ui.view.deleted.CustomDialogPetInfoNa
import com.android.petid.viewmodel.my.PetInfoViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PetInfoDetailFragment
    : BaseFragment<FragmentPetInfoDetailBinding>(FragmentPetInfoDetailBinding::inflate) {

    companion object{
        fun newInstance()= PetInfoDetailFragment()
    }

    private val viewModel: PetInfoViewModel by activityViewModels()

    private val TAG = "PetInfoDetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetInfoDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
            onBackClick = { activity?.finish() },
            showUpdateButton = true,
            onUpdateClick = {
                findNavController().navigate(R.id.action_petInfoDetailFragment_to_petInfoUpdateFragment)
            },
            title = getString(R.string.pet_info_title),
        )
        initComponent()
        viewModel.getPetDetails()
        observeGetPetInfoState()
        observeGetPetImageState()
    }

    private fun initComponent() {
        with(binding) {
            when(getPreferencesControl().getIntValue(Constants.SHARED_PET_ID_VALUE)) {
                -1 -> {
                    textViewPetidStatusNull.visibility = View.VISIBLE
                    textViewPetidStatusHasData.visibility = View.GONE
                }
                else -> {
                    textViewPetidStatusNull.visibility = View.GONE
                    textViewPetidStatusHasData.visibility = View.VISIBLE
                }
            }

            // 미등록 상태, dialog 보여주기
            textViewPetidStatusNull.setOnClickListener {
                CustomDialogCommon(
                    title = getString(R.string.pet_info_dialog_chip_na_desc),
                    boldTitle = getString(R.string.pet_info_dialog_chip_na_bold_title),
                    isSingleButton = true,
                    singleButtonText = getString(R.string.pet_info_dialog_chip_na_button)
                ).show(childFragmentManager, null)
            }
        }
    }
    /**
     * viewModel.getPetDetails 결과값 view 반영
     */
    private fun observeGetPetInfoState() {
        lifecycleScope.launch {
            viewModel.getPetDetailsResult.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        with(result.data) {
                            binding.apply {
                                textViewName.text = petName
                                textViewBirth.text = petBirthDate
                                textViewGender.text = petSex
                                textViewType.text = appearance.breed
                                textViewWeight.text =
                                    String.format(getString(R.string.to_kg), appearance.weight)
                                textViewFeature.text =
                                    listOf(appearance.hairColor, appearance.hairLength).joinToString(", ")
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
     * viewModel.getPetImageUrl 결과값 view 반영
     */
    private fun observeGetPetImageState() {
        lifecycleScope.launch {
            viewModel.getPetImageUrlResult.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        R.color.d9.let {
                            Glide.with(requireContext())
                                .load(result.data)
                                .placeholder(it)
                                .error(it)
                                .into(binding.imageViewProfile)
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