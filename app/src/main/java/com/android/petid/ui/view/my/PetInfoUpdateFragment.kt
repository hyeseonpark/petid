package com.android.petid.ui.view.my

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.android.petid.R
import com.android.petid.databinding.FragmentMyMainBinding
import com.android.petid.databinding.FragmentPetInfoUpdateBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.viewmodel.my.PetInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PetInfoUpdateFragment : Fragment() {
    lateinit var binding: FragmentPetInfoUpdateBinding
    private val viewModel: PetInfoViewModel by activityViewModels()

    private val TAG = "PetInfoUpdateFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPetInfoUpdateBinding.inflate(inflater)
        initComponent()
        return binding.root
    }

    fun initComponent() {

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
                }
            }
        }
    }
}