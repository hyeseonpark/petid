package com.android.petid.ui.view.hospital

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.core.content.ContextCompat
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.domain.entity.HospitalEntity
import com.android.domain.entity.LocationEntity
import com.android.petid.R
import com.android.petid.common.Constants.LOCATION_EUPMUNDONG_TYPE
import com.android.petid.common.Constants.LOCATION_SIDO_TYPE
import com.android.petid.common.Constants.LOCATION_SIGUNGU_TYPE
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.databinding.FragmentHospitalMainBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.ui.view.common.flowTextWatcher
import com.android.petid.ui.view.hospital.adapter.HospitalListAdapter
import com.android.petid.util.hideKeyboardAndClearFocus
import com.android.petid.viewmodel.hospital.HospitalMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private var currentHospitalList : List<HospitalEntity>? = null

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when(isGranted) {
                true -> viewModel.getSingleLocation()
                false -> viewModel.getSidoList()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHospitalMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            title = getString(R.string.main_hospital_title),
        )

        // 위치 권한 확인
        if (ContextCompat.checkSelfPermission(
                getGlobalContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한 부여 안되어 있음
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // 이미 권한 부여되어있으면 바로 위치 정보 가져오기
            viewModel.getSingleLocation()
        }

        initComponent()
        initObserve()

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

    private fun initComponent() {
        with(binding) {
            // 검색 기능
            editTextSeacrh
                .flowTextWatcher()
                .debounce(300)
                .onEach {

                    val filteredList: List<HospitalEntity>? =
                        when(val text = editTextSeacrh.text?.trim()) {
                            "" -> currentHospitalList
                            else -> {
                                currentHospitalList?.filter{ hospital ->
                                    hospital.name.contains(text!!, ignoreCase = true)
                                }
                            }
                        }
                    CoroutineScope(Dispatchers.Main).launch {
                        when(filteredList?.size) {
                            0 -> {
                                textNoResult.visibility = View.VISIBLE
                                layoutData.visibility = View.GONE
                            }
                            else -> {
                                textNoResult.visibility = View.GONE
                                layoutData.visibility = View.VISIBLE
                                hospitalListAdapter.submitList(filteredList)
                            }
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            editTextSeacrh.setOnEditorActionListener { _, actionId, _ ->
                var check = false
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    requireActivity().hideKeyboardAndClearFocus()
                    check = true
                }
                check
            }

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

    private fun initObserve() {
        observeCurrentSidoState()
        observeCurrentSigunguState()
        observeCurrentEupmundongState()
        observeCurrentHospitalListState()
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
                        currentHospitalList = result.data
                        hospitalListAdapter.submitList(currentHospitalList)

                        binding.buttonFilter.text = getString(
                            when(viewModel.currentLat == null && viewModel.currentLon == null) {
                                true -> R.string.filter_by_name
                                false -> R.string.filter_by_location
                            })
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