package com.android.petid.ui.view.hospital

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.domain.entity.LocationEntity
import com.android.petid.common.Constants.LOCATION_SIDO_TYPE
import com.android.petid.common.Constants.LOCATION_SIGUNGU_TYPE
import com.android.petid.databinding.FragmentHospitalMainBinding
import com.android.petid.ui.view.hospital.adapter.HospitalListAdapter
import com.android.petid.ui.view.hospital.item.HospitalItem
import com.android.petid.viewmodel.hospital.HospitalMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HospitalMainFragment : Fragment() {
    lateinit var binding: FragmentHospitalMainBinding
    private val viewModel: HospitalMainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHospitalMainBinding.inflate(inflater)

        initHospitalList()
        initComponent()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCurrentSidoState()
        observeCurrentSigunguState()

        requireActivity().supportFragmentManager.setFragmentResultListener(
            "locationItemSelected", this
        ) { _, bundle ->
            val selectedItem = bundle.getParcelable<LocationEntity>("selectedItem")
            val locationType = bundle.getInt("locationType")

            selectedItem?.let {
                if (locationType == LOCATION_SIDO_TYPE) {
                    viewModel.updateCurrentSidoState(it)
                } else if (locationType == LOCATION_SIGUNGU_TYPE) {
                     viewModel.updateCurrentSigunguState(it)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getSidoList()
    }


    private fun initComponent() {
        binding.buttonSido.setOnClickListener{
            viewModel.currentSidoList?.let { data -> modalBottomSheet(LOCATION_SIDO_TYPE, data) }
        }

        binding.buttonSigungu.setOnClickListener{
            viewModel.currentSigunguList?.let { data -> modalBottomSheet(LOCATION_SIGUNGU_TYPE, data) }
        }
    }

    fun newInstant() : HospitalMainFragment
    {
        val args = Bundle()
        val frag = HospitalMainFragment()
        frag.arguments = args
        return frag
    }

    private fun initHospitalList() {
        binding.recyclerviewHospitalList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewHospitalList.addItemDecoration(
            DividerItemDecoration(context, LinearLayout.VERTICAL))

        val hospitalList = arrayListOf<HospitalItem>()
        hospitalList.add(
            HospitalItem(
            1, "https://bit.ly/3yAt3za",
            "서울특별시 서초구 방배동 758번지 2호 삼호상가 109호",
            "한나동물병원", "08:00 - 18:00", "02-595-8875", "김땡땡")
        )
        hospitalList.add(
            HospitalItem(
            2, "https://bit.ly/3yAt3za",
            "서울특별시 서초구 방배동 758번지 2호 삼호상가 109호",
            "한나동물병원", "08:00 - 18:00", "02-595-8875", "박땡땡")
        )

        val hospitalListAdapter = activity?.let {
            HospitalListAdapter(hospitalList, it) {item ->
                val intent = Intent(activity, HospitalDetailActivity::class.java)
                startActivity(intent)
            } }
        binding.recyclerviewHospitalList.adapter = hospitalListAdapter

    }

    private fun observeCurrentSidoState() {
        lifecycleScope.launch {
            viewModel.currentSidoState.collect { sido ->
                sido?.let {
                    val name = it.name
                    binding.buttonSido.text = name
                }
            }
        }
    }

    private fun observeCurrentSigunguState() {
        lifecycleScope.launch {
            viewModel.currentSigunguState.collect { sido ->
                sido?.let {
                    val name = it.name
                    binding.buttonSigungu.text = name
                }
            }
        }
    }

    private fun modalBottomSheet(locationType: Int, locationList: List<LocationEntity>) {
        val bottomSheet = LocationBottomSheetFragment.newInstance(locationType, locationList)
        bottomSheet.show(requireActivity().supportFragmentManager, LocationBottomSheetFragment.TAG)
    }
}