package com.android.petid.ui.view.hospital

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.petid.R
import com.android.petid.databinding.FragmentHospitalBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class HospitalFragment : Fragment() {
    lateinit var binding: FragmentHospitalBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHospitalBinding.inflate(inflater)

        initHospitalList()
        initComponent()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().supportFragmentManager.setFragmentResultListener(
            "sigunguKey", this) { _, bundle ->
            val result = bundle.getString("bundleKey")
            binding.buttonSigungu.text = result
        }
    }

    private fun initComponent() {
        binding.buttonSigungu.setOnClickListener{
            modalBottomSheet()
        }
    }

    fun newInstant() : HospitalFragment
    {
        val args = Bundle()
        val frag = HospitalFragment()
        frag.arguments = args
        return frag
    }

    private fun initHospitalList() {
        binding.recyclerviewHospitalList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewHospitalList.addItemDecoration(
            DividerItemDecoration(context, LinearLayout.VERTICAL))

        val hospitalList = arrayListOf<HospitalItem>()
        hospitalList.add(HospitalItem(
            1, "https://bit.ly/3yAt3za",
            "서울특별시 서초구 방배동 758번지 2호 삼호상가 109호",
            "한나동물병원", "08:00 - 18:00", "02-595-8875", "김땡땡"))
        hospitalList.add(HospitalItem(
            2, "https://bit.ly/3yAt3za",
            "서울특별시 서초구 방배동 758번지 2호 삼호상가 109호",
            "한나동물병원", "08:00 - 18:00", "02-595-8875", "박땡땡"))

        val hospitalListAdapter = activity?.let {
            HospitalListAdapter(hospitalList, it) {item ->
                val intent = Intent(activity, HospitalDetailActivity::class.java)
                startActivity(intent)
            } }
        binding.recyclerviewHospitalList.adapter = hospitalListAdapter

    }

    private fun modalBottomSheet() {
        val bottomSheet = LocationBottomSheetFragment()
        bottomSheet.show(requireActivity().supportFragmentManager, LocationBottomSheetFragment.TAG)
    }
}