package com.android.petid.ui.view.generate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.databinding.FragmentPetIdStartBinding

/**
 *
 */
class PetIdStartFragment : Fragment() {
    private lateinit var binding: FragmentPetIdStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPetIdStartBinding.inflate(inflater)
        initComponent()

        return binding.root
    }

    fun initComponent() {
        with (binding) {
            radioButtonGroup.setOnCheckedChangeListener { _, checkId ->
                when(checkId != -1) {
                    true -> buttonNext.isEnabled = true
                    false -> buttonNext.isEnabled = false
                }
            }
            buttonNext.setOnClickListener{
                findNavController().navigate(R.id.action_petIdStartFragment_to_userInfoInputFragment)
            }
        }
    }
}