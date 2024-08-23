package com.android.petid.view.hospital

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.petid.R
import com.android.petid.databinding.FragmentLocationBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class LocationBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentLocationBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBottomSheetBinding.inflate(inflater)
        initHospitalList()

        return binding.root
    }

    companion object {
        const val TAG = "LocationBottomSheetFragment"
    }

    private fun initHospitalList() {
        binding.recyclerviewLocationList.layoutManager = LinearLayoutManager(requireContext())

        val locationList = arrayListOf<LocationItem>()
        locationList.add(LocationItem(1, "서초구"))
        locationList.add(LocationItem(2, "송파구"))
        locationList.add(LocationItem(3, "서초구"))
        locationList.add(LocationItem(4, "서초구"))
        locationList.add(LocationItem(5, "서초구"))
        locationList.add(LocationItem(3, "서초구"))
        locationList.add(LocationItem(4, "서초구"))
        locationList.add(LocationItem(5, "서초구"))

        val locationListAdapter = activity?.let {
            LocationListAdapter(locationList, it) {item ->
                val result = item.location
                setFragmentResult("sigunguKey", bundleOf("bundleKey" to result))
                dismiss()
            } }
        binding.recyclerviewLocationList.adapter = locationListAdapter

    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val bottomSheet =
                dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true

            val layoutParams = bottomSheet!!.layoutParams
            layoutParams.height = getWindowHeight() * 45 / 100
            bottomSheet.layoutParams = layoutParams
        }
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}