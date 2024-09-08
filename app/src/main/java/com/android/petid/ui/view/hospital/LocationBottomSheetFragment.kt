package com.android.petid.ui.view.hospital

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.domain.entity.LocationEntity
import com.android.petid.common.Constants
import com.android.petid.databinding.FragmentLocationBottomSheetBinding
import com.android.petid.ui.view.hospital.adapter.LocationListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class LocationBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var binding: FragmentLocationBottomSheetBinding
    private var locationList: List<LocationEntity>? = null
    private var locationType: Int? = null

    companion object {
        const val TAG = "LocationBottomSheetFragment"

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

        locationList = arguments?.getParcelableArrayList("locationList")
        locationType = arguments?.getInt("locationType")

        if (locationType == Constants.LOCATION_SIDO_TYPE) {
            binding.textViewTitle.text = "시/도"
        } else if (locationType == Constants.LOCATION_SIGUNGU_TYPE) {
            binding.textViewTitle.text = "시/군/구"
        }

        initLocationList()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initLocationList() {
        binding.recyclerviewLocationList.layoutManager = LinearLayoutManager(requireContext())

        locationList?.let { list ->
            val locationItems = list.map { LocationEntity(it.id, it.name) }

            val locationListAdapter = activity?.let {
                LocationListAdapter(ArrayList(locationItems), it) { item ->
                    val result = Bundle().apply {
                        putParcelable("selectedItem", item)
                        putInt("locationType", locationType ?: 0)
                    }
                    setFragmentResult("locationItemSelected", result)
                    dismiss()
                }
            }
            binding.recyclerviewLocationList.adapter = locationListAdapter
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