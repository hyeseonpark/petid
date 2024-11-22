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
import com.android.petid.common.BaseFragment
import com.android.petid.databinding.FragmentPetInfoDetailBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.viewmodel.my.PetInfoViewModel
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
        initComponent()
        // viewModel.getPetDetails()
        observeGetPetInfoState()
        return binding.root
    }

    private fun initComponent() {
        with(binding) {
            textViewUpdate.setOnClickListener{
                findNavController().navigate(R.id.action_petInfoDetailFragment_to_petInfoUpdateFragment)
            }

            textViewPetCreateStatus.setOnClickListener {
                // 미등록 상태, dialog 보여주기
                infoDialog()
            }
        }
    }

    /**
     * 내장칩 미 등록시, info dialog
     */
    private fun infoDialog() {
        val dialog = CustomDialogPetInfoNa()
        dialog.show(this.childFragmentManager, "CustomDialogPetInfoNa")
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

                            /*petImages?.takeIf { it.isNotBlank() }?.let {
                                com.bumptech.glide.Glide.with(requireContext()).load(it).into(binding.imageViewProfile)
                            }*/

                            binding.textViewName.text = petName
                            binding.textViewBirth.text = petBirthDate
                            binding.textViewGender.text = petSex
                            binding.textViewType.text = appearance.breed
                            binding.textViewWeight.text =
                                String.format(getString(R.string.to_kg), appearance.weight)
                            binding.textViewFeature.text =
                                listOf(appearance.hairColor, appearance.hairLength).joinToString(", ")
                        }
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