package com.android.petid.ui.view.hospital

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.android.petid.common.Constants.LOCATION_EUPMUNDONG_TYPE
import com.android.petid.common.Constants.LOCATION_SIDO_TYPE
import com.android.petid.common.Constants.LOCATION_SIGUNGU_TYPE
import com.android.petid.databinding.FragmentHospitalMainBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.hospital.adapter.HospitalListAdapter
import com.android.petid.viewmodel.hospital.HospitalMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HospitalMainFragment : Fragment() {
    lateinit var binding: FragmentHospitalMainBinding
    private val viewModel: HospitalMainViewModel by activityViewModels()

    private val TAG = "HospitalMainFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHospitalMainBinding.inflate(inflater)

        initComponent()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCurrentSidoState()
        observeCurrentSigunguState()
        observeCurrentEupmundongState()

        observeCurrentHospitalListState()

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
                } else if (locationType == LOCATION_EUPMUNDONG_TYPE) {
                    viewModel.updateCurrentEupmundongState(it)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getSidoList()
    }


    private fun initComponent() {
        binding.recyclerviewHospitalList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerviewHospitalList.addItemDecoration(
            DividerItemDecoration(context, LinearLayout.VERTICAL))

        binding.buttonSido.setOnClickListener{
            viewModel.currentSidoList?.let { data -> modalBottomSheet(LOCATION_SIDO_TYPE, data) }
        }

        binding.buttonSigungu.setOnClickListener{
            viewModel.currentSigunguList?.let { data -> modalBottomSheet(LOCATION_SIGUNGU_TYPE, data) }
        }

        binding.buttonEupmundong.setOnClickListener{
            viewModel.currentEupmundongList?.let { data -> modalBottomSheet(LOCATION_EUPMUNDONG_TYPE, data) }
        }
    }

    fun newInstant() : HospitalMainFragment
    {
        val args = Bundle()
        val frag = HospitalMainFragment()
        frag.arguments = args
        return frag
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
            viewModel.currentSigunguState.collect { sigungu ->
                sigungu?.let {
                    val name = it.name
                    binding.buttonSigungu.text = name
                }
            }
        }
    }

    private fun observeCurrentEupmundongState() {
        lifecycleScope.launch {
            viewModel.currentEupmundongState.collect { eupmundong ->
                eupmundong?.let {
                    val name = it.name
                    binding.buttonEupmundong.text = name
                }
            }
        }
    }

    /**
     * 병원 리스트 조회
     */
    private fun observeCurrentHospitalListState() {
        lifecycleScope.launch {
            viewModel.hospitalApiState.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        val hospitalList = result.data

                        if (hospitalList != null) {
                            val hospitalListAdapter = HospitalListAdapter(hospitalList, requireActivity()) { item ->
                                val intent = Intent(activity, HospitalDetailActivity::class.java)
                                    .putExtra("hospitalDetail", item)
                                startActivity(intent)
                            }
                            binding.recyclerviewHospitalList.adapter = hospitalListAdapter
                        }
                    }
                    is CommonApiState.Error -> {
                        // 오류 처리
                        Log.e(TAG, "Login error: ${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        // 로딩 상태 처리
                        Log.d(TAG, "Loading....................")
                    }
                }
            }
        }
    }
    private fun modalBottomSheet(locationType: Int, locationList: List<LocationEntity>) {
        val bottomSheet = LocationBottomSheetFragment.newInstance(locationType, locationList)
        bottomSheet.show(requireActivity().supportFragmentManager, LocationBottomSheetFragment.TAG)
    }
}