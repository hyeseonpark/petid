package com.android.petid.ui.view.my

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.android.petid.R
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentPetInfoUpdateBinding
import com.android.petid.ui.component.CustomDialogCommon
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.deleted.CustomDialogPetInfoNa
import com.android.petid.viewmodel.my.PetInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PetInfoUpdateFragment
    : BaseFragment<FragmentPetInfoUpdateBinding>(FragmentPetInfoUpdateBinding::inflate) {

    companion object{
        fun newInstance()= PetInfoUpdateFragment()
    }

    private val viewModel: PetInfoViewModel by activityViewModels()

    private val TAG = "PetInfoUpdateFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetInfoUpdateBinding.inflate(inflater)
        initComponent()
        observeGetMemberInfoState()
        return binding.root
    }

    fun initComponent() {
        with(binding) {
            buttonNoRegister.setOnClickListener {
                CustomDialogCommon(
                    title = getString(R.string.pet_info_dialog_chip_na_desc),
                    boldTitle = getString(R.string.pet_info_dialog_chip_na_bold_title),
                    isSingleButton = true,
                    singleButtonText = getString(R.string.pet_info_dialog_chip_na_button)
                ).show(childFragmentManager, null)
            }
        }
    }

    private fun observeGetMemberInfoState() {
        lifecycleScope.launch {
            viewModel.getPetDetailsResult.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        with(result.data) {
                            binding.editTextName.setText(petName)
                            binding.editTextBirth.setText(petBirthDate)
                            when(petNeuteredYn) {
                                "Y" -> {
                                    binding.editNeuteringDate.isEnabled = false
                                    binding.editNeuteringDate.setText(petNeuteredDate)
                                }
                                "N" -> {
                                    binding.editNeuteringDate.isEnabled = true
                                }
                            }
                            binding.editTextWeight.setText(
                                String.format(getString(R.string.to_kg), appearance.weight))

                            binding.editTextFeature.setText(
                                listOf(appearance.hairColor, appearance.hairLength)
                                    .joinToString(", "))

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