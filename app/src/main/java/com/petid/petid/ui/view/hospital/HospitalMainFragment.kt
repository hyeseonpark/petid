package com.petid.petid.ui.view.hospital

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.petid.domain.entity.HospitalEntity
import com.petid.domain.entity.LocationEntity
import com.petid.petid.R
import com.petid.petid.common.Constants.LOCATION_EUPMUNDONG_TYPE
import com.petid.petid.common.Constants.LOCATION_SIDO_TYPE
import com.petid.petid.common.Constants.LOCATION_SIGUNGU_TYPE
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.databinding.FragmentHospitalMainBinding
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.ui.view.common.flowTextWatcher
import com.petid.petid.ui.view.hospital.adapter.HospitalListAdapter
import com.petid.petid.util.TAG
import com.petid.petid.util.hideKeyboardAndClearFocus
import com.petid.petid.util.showErrorMessage
import com.petid.petid.util.throttleFirst
import com.petid.petid.viewmodel.hospital.HospitalMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.editorActions


/**
 * 메인 > 등록대행병원
 */
@AndroidEntryPoint
class HospitalMainFragment : BaseFragment<FragmentHospitalMainBinding>(FragmentHospitalMainBinding::inflate) {

    private val viewModel: HospitalMainViewModel by activityViewModels()

    private lateinit var hospitalListAdapter : HospitalListAdapter

    private var currentHospitalList : List<HospitalEntity>? = null

    private val SEARCH_DEBOUNCE_DELAY_MS = 500L

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

        // 기존에 불러온 데이터가 없는 경우만 초기 데이터 불러오기
        if(viewModel.hospitalApiState.value == CommonApiState.Init) {
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
                .debounce(SEARCH_DEBOUNCE_DELAY_MS)
                .onEach {
                    val filteredList = getFilteredList(editTextSeacrh.text.toString())
                    updateListUi(filteredList)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            editTextSeacrh
                .editorActions()
                .onEach {
                    var check = false
                    if (it == EditorInfo.IME_ACTION_DONE) {
                        requireActivity().hideKeyboardAndClearFocus()
                        check = true
                    }
                    check
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

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

            buttonSido
                .clicks()
                .throttleFirst()
                .onEach {
                    viewModel.currentSidoList?.let { data -> modalBottomSheet(LOCATION_SIDO_TYPE, data) }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            buttonSigungu
                .clicks()
                .throttleFirst()
                .onEach {
                    viewModel.currentSigunguList?.let { data -> modalBottomSheet(LOCATION_SIGUNGU_TYPE, data) }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            buttonEupmundong
                .clicks()
                .throttleFirst()
                .onEach {
                    viewModel.currentEupmundongList?.let { data -> modalBottomSheet(LOCATION_EUPMUNDONG_TYPE, data) }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

        }
    }

    /**
     * get filtered list
     */
    private fun getFilteredList(text: String): List<HospitalEntity>? = when(text.trim().isEmpty()) {
        true -> currentHospitalList
        false -> {
            currentHospitalList?.filter{ hospital ->
                hospital.name.contains(text, ignoreCase = true)
            }
        }
    }

    /**
     * update list ui
     */
    private fun updateListUi(filteredList: List<HospitalEntity>?) {
        with(binding) {
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
                    binding.buttonEupmundong.text = it.name
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
                        binding.editTextSeacrh.text?.clear()
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
                }

                if (result !is CommonApiState.Loading)
                    hideLoading()
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