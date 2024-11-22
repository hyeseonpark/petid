package com.android.petid.ui.view.hospital

import android.content.Intent
import android.os.Build
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
import com.android.petid.common.BaseFragment
import com.android.petid.common.Constants.LOCATION_EUPMUNDONG_TYPE
import com.android.petid.common.Constants.LOCATION_SIDO_TYPE
import com.android.petid.common.Constants.LOCATION_SIGUNGU_TYPE
import com.android.petid.databinding.FragmentBlogMainBinding
import com.android.petid.databinding.FragmentHospitalMainBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.blog.BlogMainFragment
import com.android.petid.ui.view.hospital.adapter.HospitalListAdapter
import com.android.petid.viewmodel.hospital.HospitalMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 메인 > 등록대행병원
 */
@AndroidEntryPoint
class HospitalMainFragment : BaseFragment<FragmentHospitalMainBinding>(FragmentHospitalMainBinding::inflate) {

    companion object{
        fun newInstance() = HospitalMainFragment()
    }

    private val viewModel: HospitalMainViewModel by activityViewModels()

    private val TAG = "HospitalMainFragment"

    private lateinit var hospitalListAdapter : HospitalListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHospitalMainBinding.inflate(inflater)

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

            val selectedItem = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("selectedItem", LocationEntity::class.java)!!
            } else {
                (bundle.getParcelable("selectedItem") as? LocationEntity)!!
            }

            val locationType = bundle.getInt("locationType")

            selectedItem.let {
                when(locationType) {
                    LOCATION_SIDO_TYPE -> viewModel.updateCurrentSidoState(it)
                    LOCATION_SIGUNGU_TYPE -> viewModel.updateCurrentSigunguState(it)
                    LOCATION_EUPMUNDONG_TYPE -> viewModel.updateCurrentEupmundongState(it)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getSidoList()
    }


    private fun initComponent() {
        with(binding) {
            hospitalListAdapter = HospitalListAdapter(requireActivity()) { item ->
                val intent = Intent(activity, HospitalActivity::class.java)
                    .putExtra("hospitalDetail", item)
                startActivity(intent)
            }
            recyclerviewHospitalList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(
                    DividerItemDecoration(context, LinearLayout.VERTICAL))
                adapter = hospitalListAdapter
            }

            buttonSido.setOnClickListener{
                viewModel.currentSidoList?.let { data -> modalBottomSheet(LOCATION_SIDO_TYPE, data) }
            }

            buttonSigungu.setOnClickListener{
                viewModel.currentSigunguList?.let { data -> modalBottomSheet(LOCATION_SIGUNGU_TYPE, data) }
            }

            buttonEupmundong.setOnClickListener{
                viewModel.currentEupmundongList?.let { data -> modalBottomSheet(LOCATION_EUPMUNDONG_TYPE, data) }
            }

        }
    }

    /**
     * 시,도 결과값
     */
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

    /**
     * 시,군,구 결과값
     */
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

    /**
     * 읍,면,동 결과값
     */
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
                        hospitalListAdapter.submitList(hospitalList)
                    }
                    is CommonApiState.Error -> {
                        // 오류 처리
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        // 로딩 상태 처리
                        Log.d(TAG, "Loading....................")
                    }
                    is CommonApiState.Init -> {}
                }
            }
        }
    }

    /**
     * 하단에서 올라오는 modal 구현
     */
    private fun modalBottomSheet(locationType: Int, locationList: List<LocationEntity>) {
        val bottomSheet = LocationBottomSheetFragment.newInstance(locationType, locationList)
        bottomSheet.show(requireActivity().supportFragmentManager, LocationBottomSheetFragment.TAG)
    }
}