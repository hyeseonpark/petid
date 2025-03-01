package com.petid.petid.ui.view.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.petid.petid.R
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.databinding.FragmentCheckingInfoBinding
import com.petid.petid.util.booleanCharToSign
import com.petid.petid.util.genderCharToString
import com.petid.petid.util.showErrorMessage
import com.petid.petid.util.throttleFirst
import com.petid.petid.viewmodel.generate.GeneratePetidSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

@AndroidEntryPoint
class CheckingInfoFragment : BaseFragment<FragmentCheckingInfoBinding>(FragmentCheckingInfoBinding::inflate) {
    private val viewModel: GeneratePetidSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckingInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
        )
        initComponent()
    }

    fun initComponent() {
        with(binding) {
            buttonNext
                .clicks()
                .throttleFirst()
                .onEach {
                    findNavController().navigate(R.id.action_checkingInfoFragment_to_signatureFragment)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            runCatching {
                viewModel.petInfo.build(false).also {
                    textViewName.text = it.petName
                    textViewBirth.text = it.petBirthDate
                    textViewGender.text =
                        listOf(genderCharToString(it.petSex),
                            String.format(getString(R.string.neutering), booleanCharToSign(it.petNeuteredYn)))
                            .joinToString(", ")
                    textViewBreed.text = it.appearance.breed
                    textViewAppearance.text =
                        listOf(it.appearance.hairColor, it.appearance.hairLength).joinToString(", ")
                    textViewWeight.text =
                        String.format(getString(R.string.to_kg),it.appearance.weight)
                }
            }.onFailure {
                showErrorMessage(it.message.toString())
            }
        }
    }
}