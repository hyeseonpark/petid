package com.petid.petid.ui.view.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.petid.petid.R
import com.petid.petid.common.Constants.CHIP_TYPE
import com.petid.petid.databinding.FragmentPetIdStartBinding
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.util.throttleFirst
import com.petid.petid.viewmodel.generate.GeneratePetidSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.checkedChanges

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
            radioButtonGroup
                .checkedChanges()
                .onEach {
                    buttonNext.isEnabled = it != -1

                    selectedChipIdx = when(it) {
                        buttonNa.id -> 0
                        buttonExternal.id -> 1
                        buttonInternal.id -> 2
                        else -> -1
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            buttonNext
                .clicks()
                .throttleFirst()
                .onEach {
                    viewModel.petInfo.setChipType(chipTypes[selectedChipIdx])
                    findNavController().navigate(R.id.action_petIdStartFragment_to_userInfoInputFragment)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

        }
    }
}