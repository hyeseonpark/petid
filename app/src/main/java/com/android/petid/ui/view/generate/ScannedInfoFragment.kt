package com.android.petid.ui.view.generate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.databinding.FragmentScannedInfoBinding

class ScannedInfoFragment : Fragment() {
    private lateinit var binding: FragmentScannedInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScannedInfoBinding.inflate(layoutInflater)
        initComponent()

        return binding.root
    }

    fun initComponent() {
        binding.buttonNext.button.setOnClickListener{
            findNavController().navigate(R.id.action_scannedInfoFragment_to_checkingInfoFragment)
        }
    }
}