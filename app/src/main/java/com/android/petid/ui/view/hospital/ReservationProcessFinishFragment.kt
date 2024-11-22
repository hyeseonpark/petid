package com.android.petid.ui.view.hospital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.android.petid.common.BaseFragment
import com.android.petid.databinding.FragmentReservationProcessFinishBinding

class ReservationProcessFinishFragment: BaseFragment<FragmentReservationProcessFinishBinding>(
    FragmentReservationProcessFinishBinding::inflate) {

    companion object{
        fun newInstance()= ReservationProcessFinishFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationProcessFinishBinding.inflate(layoutInflater)

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