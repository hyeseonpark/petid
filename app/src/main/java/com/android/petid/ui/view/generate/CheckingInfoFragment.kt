package com.android.petid.ui.view.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentCheckingInfoBinding
import com.android.petid.util.booleanCharToSign
import com.android.petid.util.genderCharToString
import com.android.petid.viewmodel.generate.GeneratePetidSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckingInfoFragment : BaseFragment<FragmentCheckingInfoBinding>(FragmentCheckingInfoBinding::inflate) {

    companion object{
        fun newInstance()= CheckingInfoFragment()
    }

    private val viewModel: GeneratePetidSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckingInfoBinding.inflate(layoutInflater)
        initComponent()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewModel.petInfo.build()) {
            binding.textViewName.text = petName
            binding.textViewBirth.text = petBirthDate
            binding.textViewGender.text =
                listOf(genderCharToString(petSex!!),
                    String.format(getString(R.string.neutering), booleanCharToSign(petNeuteredYn!!)))
                    .joinToString(", ")
            binding.textViewBreed.text = appearance!!.breed
            binding.textViewAppearance.text =
                listOf(appearance!!.hairColor, appearance!!.hairLength).joinToString(", ")
            binding.textViewWeight.text =
                String.format(getString(R.string.to_kg),appearance!!.weight)
        }
    }

    fun initComponent() {
        binding.buttonNext.setOnClickListener{
            findNavController().navigate(R.id.action_checkingInfoFragment_to_signatureFragment)
        }
    }
}