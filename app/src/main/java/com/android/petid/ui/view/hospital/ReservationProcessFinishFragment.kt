package com.android.petid.ui.view.hospital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.android.petid.databinding.FragmentReservationProcessFinishBinding

class ReservationProcessFinishFragment : Fragment() {
    private lateinit var binding: FragmentReservationProcessFinishBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReservationProcessFinishBinding.inflate(layoutInflater)

        initComponent()

        return binding.root

    }

    fun initComponent() {

        // 뒤로가기 버튼 선택 시 Activity 종료
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        binding.buttonConfirm.setOnClickListener{
            requireActivity().finish()
        }
    }
}