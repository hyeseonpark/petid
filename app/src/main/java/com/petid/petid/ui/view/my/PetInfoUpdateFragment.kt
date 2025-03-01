package com.petid.petid.ui.view.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.petid.petid.R
import com.petid.petid.databinding.FragmentPetInfoUpdateBinding
import com.petid.petid.ui.component.CustomDialogCommon
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.util.showDatePicker
import com.petid.petid.util.showErrorMessage
import com.petid.petid.util.throttleFirst
import com.petid.petid.viewmodel.my.PetInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

@AndroidEntryPoint
class PetInfoUpdateFragment
    : BaseFragment<FragmentPetInfoUpdateBinding>(FragmentPetInfoUpdateBinding::inflate) {

    private val viewModel: PetInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetInfoUpdateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
            title = getString(R.string.pet_info_update_title),
        )
        observeGetMemberInfoState()

        initComponent()
    }

    fun initComponent() {
        with(binding) {
            editTextNeuteringDate
                .clicks()
                .throttleFirst()
                .onEach {
                    showDatePicker(editTextNeuteringDate, requireContext())
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            buttonNoRegister
                .clicks()
                .throttleFirst()
                .onEach {
                    CustomDialogCommon(
                        title = getString(R.string.pet_info_dialog_chip_na_desc),
                        boldTitle = getString(R.string.pet_info_dialog_chip_na_bold_title),
                        isSingleButton = true,
                        singleButtonText = getString(R.string.pet_info_dialog_chip_na_button)
                    ).show(childFragmentManager, null)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            buttonComplete
                .clicks()
                .throttleFirst()
                .onEach {
                    completeDialog()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    /**
     * pet info update dialog
     */
    private fun completeDialog() {
        val dialog = CustomDialogCommon(
            getString(R.string.update_complete_dialog), {
                with(binding) {
                    val neuteredDate = editTextNeuteringDate.text.toString().takeIf { it.isNotBlank() }
                    val weight = editTextWeight.text.toString().toIntOrNull()
                    viewModel.updatePetInfo(neuteredDate, weight)
                }
            })

        dialog.show(this.childFragmentManager, "CustomDialogCommon")
    }

    /**
     * viewModel.getPetDetailsResult 결과값 view 반영
     */
    private fun observeGetMemberInfoState() {
        lifecycleScope.launch {
            viewModel.getPetDetailsResult.collectLatest { result ->
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> {
                        with(result.data) {
                            binding.editTextName.setText(petName)
                            binding.editTextBirth.setText(petBirthDate)
                            when(petNeuteredYn) {
                                getString(R.string.Y) -> {
                                    binding.editTextNeuteringDate.isEnabled = false
                                    binding.editTextNeuteringDate.setText(petNeuteredDate)
                                }
                                getString(R.string.N) -> {
                                    binding.editTextNeuteringDate.isEnabled = true
                                }
                            }
                            binding.editTextWeight.setText("${appearance.weight}")

                            binding.editTextFeature.setText(
                                listOf(appearance.hairColor, appearance.hairLength)
                                    .joinToString(", "))

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