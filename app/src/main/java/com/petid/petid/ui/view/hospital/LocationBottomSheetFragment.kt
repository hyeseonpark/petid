package com.petid.petid.ui.view.hospital

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.petid.R
import com.petid.petid.common.Constants.LOCATION_EUPMUNDONG_TYPE
import com.petid.petid.common.Constants.LOCATION_SIDO_TYPE
import com.petid.petid.common.Constants.LOCATION_SIGUNGU_TYPE
import com.petid.petid.databinding.FragmentLocationBottomSheetBinding
import com.petid.petid.ui.view.hospital.adapter.LocationListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class LocationBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentLocationBottomSheetBinding
    private var locationList: List<LocationEntity>? = null
    private var locationType: Int? = null

    private lateinit var locationListAdapter : LocationListAdapter

    companion object {
        fun newInstance(locationType: Int, locationList: List<LocationEntity>): LocationBottomSheetFragment {
            val fragment = LocationBottomSheetFragment()
            val args = Bundle().apply {
                putInt("locationType", locationType)
                putParcelableArrayList("locationList", ArrayList(locationList))
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBottomSheetBinding.inflate(inflater)

        initLocationList()
        return binding.root
    }

    private fun initLocationList() {
        locationList = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList("locationList", LocationEntity::class.java)!!
        } else {
            arguments?.getParcelableArrayList("locationList")
        }

        locationType = arguments?.getInt("locationType")

        binding.textViewTitle.text = when(locationType) {
            LOCATION_SIDO_TYPE ->  getString(R.string.sido_title)
            LOCATION_SIGUNGU_TYPE -> getString(R.string.sigungu_title)
            LOCATION_EUPMUNDONG_TYPE -> getString(R.string.eupmundong_title)
            else -> return
        }

        locationListAdapter = LocationListAdapter(requireContext()) { item ->
            val result = Bundle().apply {
                putParcelable("selectedItem", item)
                putInt("locationType", locationType ?: 0)
            }
            setFragmentResult("locationItemSelected", result)
            dismiss()
        }

        binding.recyclerviewLocationList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = locationListAdapter
        }

        locationList?.let { list ->
            val locationItems = list.map { LocationEntity(it.id, it.name) }
            locationListAdapter.submitList(locationItems)
        }
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