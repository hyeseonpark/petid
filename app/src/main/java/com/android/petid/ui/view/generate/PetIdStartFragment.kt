package com.android.petid.ui.view.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.common.Constants.CHIP_TYPE
import com.android.petid.databinding.FragmentPetIdStartBinding
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.viewmodel.generate.GeneratePetidSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 */
@AndroidEntryPoint
class PetIdStartFragment: BaseFragment<FragmentPetIdStartBinding>(FragmentPetIdStartBinding::inflate) {

    private val viewModel: GeneratePetidSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetIdStartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
            onBackClick = { activity?.finish() },
        )
        initComponent()
    }

    fun initComponent() {
        val chipTypes = CHIP_TYPE
        with (binding) {
            var selectedChipIdx = -1
            radioButtonGroup.setOnCheckedChangeListener { _, checkedId ->
                buttonNext.isEnabled = checkedId != -1

                selectedChipIdx = when(checkedId) {
                    buttonNa.id -> 0
                    buttonExternal.id -> 1
                    buttonInternal.id -> 2
                    else -> -1
                }
            }
            buttonNext.setOnClickListener{
                viewModel.petInfo.setChipType(chipTypes[selectedChipIdx])
                findNavController().navigate(R.id.action_petIdStartFragment_to_userInfoInputFragment)
            }
        }
    }
}