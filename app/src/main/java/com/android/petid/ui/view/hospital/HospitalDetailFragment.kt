package com.android.petid.ui.view.hospital

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.domain.entity.HospitalEntity
import com.android.petid.R
import com.android.petid.databinding.FragmentHospitalDetailBinding
import com.android.petid.viewmodel.hospital.HospitalViewModel

class HospitalDetailFragment : Fragment() {
    private lateinit var binding: FragmentHospitalDetailBinding
    private val viewModel: HospitalViewModel by activityViewModels()

    private val TAG = "HospitalDetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHospitalDetailBinding.inflate(layoutInflater)

        initComponent()

        return binding.root

    }

    private fun initComponent() {
        with(viewModel.hospitalDetail) {
            binding.textViewTitle.text = name
            binding.textViewVet.text = vet
            binding.textViewTime.text = hours
            binding.textViewTel.text = tel

            binding.buttonReserve.setOnClickListener{
                findNavController().navigate(
                    R.id.action_hospitalDetailFragment_to_reservationCalendarFragment)
            }
        }
    }
}