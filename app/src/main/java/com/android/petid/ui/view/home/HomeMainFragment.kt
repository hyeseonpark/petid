package com.android.petid.ui.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.android.domain.entity.BannerEntity
import com.android.petid.BuildConfig
import com.android.petid.R
import com.android.petid.common.Constants
import com.android.petid.common.Constants.BANNER_TYPE_MAIN
import com.android.petid.common.Constants.CHIP_TYPE
import com.android.petid.common.GlobalApplication.Companion.getPreferencesControl
import com.android.petid.databinding.FragmentHomeMainBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.ui.view.generate.GeneratePetidMainActivity
import com.android.petid.ui.view.home.adapter.HomeBannerAdapter
import com.android.petid.util.booleanCharToSign
import com.android.petid.util.genderCharToString
import com.android.petid.util.calculateAge
import com.android.petid.util.showErrorMessage
import com.android.petid.util.throttleFirst
import com.android.petid.viewmodel.home.HomeMainVIewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks


@AndroidEntryPoint
class HomeMainFragment : BaseFragment<FragmentHomeMainBinding>(FragmentHomeMainBinding::inflate) {

    private val viewModel: HomeMainVIewModel by activityViewModels()

    // banner adapter
    private lateinit var bannerAdapter : HomeBannerAdapter

    private lateinit var snapHelper : PagerSnapHelper
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var snapPagerScrollListener : SnapPagerScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponent()
        setupBannerObservers()
        observeGetMemberInfoState()
        observeGetPetInfoState()
        observeGetPetImageUrlState()

        viewModel.getBannerList(BANNER_TYPE_MAIN)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMemberInfo()
    }

    override fun onStop() {
        super.onStop()

        viewModel.stopAutoScroll()
    }

    /**
     * component 초기화
     */
    private fun initComponent() {
        with(binding) {
            imageViewNoti.setOnClickListener {
                val target = Intent(activity, NotificationActivity::class.java)
                startActivity(target)
            }

            buttonCreateStart.setOnClickListener{
                val target = Intent(activity, GeneratePetidMainActivity::class.java)
                startActivity(target)
            }

            // 내장칩 미 등록자 > '지금 반려동물을 등록하세요' 버튼
            listOf(layoutRegister, layoutRegisterBack).forEach {
                it
                    .clicks()
                    .throttleFirst()
                    .onEach {
                        val target = Intent(activity, GeneratePetidMainActivity::class.java)
                        startActivity(target)
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }

            textViewFlip
                .clicks()
                .throttleFirst(800L)
                .onEach {
                    viewPetidCard.flipTheView()
                    textViewFlip.text =  when(viewPetidCard.isFrontSide) {
                        true -> getString(R.string.flip_card_back_side)
                        false -> getString(R.string.flip_card_front_side)
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            bannerAdapter = HomeBannerAdapter(requireContext())

            snapHelper = PagerSnapHelper().also {
                it.attachToRecyclerView(recyclerviewBannerList)
            }
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerviewBannerList.apply {
                layoutManager = layoutManager
                adapter = bannerAdapter
            }

            snapPagerScrollListener = SnapPagerScrollListener(
                snapHelper,
                SnapPagerScrollListener.ON_SCROLL,
                true,
                object : SnapPagerScrollListener.OnChangeListener {
                    override fun onSnapped(position: Int) {
                        //position 받아서 이벤트 처리
                        viewModel.updateCurrentPosition(position)
                    }
                }
            )
            recyclerviewBannerList.addOnScrollListener(snapPagerScrollListener)
            observeBannerPosition()

            // Debug Mode: 로고 클릭시 카드 변경
            if(BuildConfig.DEBUG) {
                imageViewLogo.setOnClickListener {
                    when(binding.viewNoPetidCard.visibility) {
                        View.VISIBLE -> setPetidCardType(CHIP_TYPE[1])
                        View.GONE -> setPetidCardType(null)
                    }
                }
            }
        }
    }

    /**
     * @param type CHIP_TYPE 에 따른 펫아이디 카드
     */
    private fun setPetidCardType(type: String?) {
        with(binding) {
            when(type) {
                CHIP_TYPE[0], CHIP_TYPE[1] -> {
                    viewNoPetidCard.visibility = View.GONE
                    viewPetidCard.visibility = View.VISIBLE
                    layoutRegister.visibility = View.VISIBLE
                }
                CHIP_TYPE[2] -> {
                    viewNoPetidCard.visibility = View.GONE
                    viewPetidCard.visibility = View.VISIBLE
                    layoutRegister.visibility = View.GONE
                }
                null -> {
                    viewNoPetidCard.visibility = View.VISIBLE
                    viewPetidCard.visibility = View.GONE
                }
            }
        }
    }

    /**
     * 배너 초기화
     */
    private fun initBanner(bannerList: List<BannerEntity>) {
        bannerAdapter.submitList(bannerList)

        // bannerList의 총 개수를 binding.textViewTotalPage.text에 설정
        val total = bannerList.size
        binding.textViewTotalPage.text = "$total"

        // 자동 스크롤 시작
        viewModel.startAutoScroll()
    }

    /**
     * Banner Position observe
     */
    private fun observeBannerPosition() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bannerScrollPosition.collectLatest { position ->
                    binding.recyclerviewBannerList.smoothScrollToPosition(position)
                    binding.textViewCurrentPage.text = "${position % bannerAdapter.getListSize() + 1}"
                }
            }
        }
    }

    /**
     * banner api observer
     */
    private fun setupBannerObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bannerApiState.collectLatest { result ->
                    if (result !is CommonApiState.Loading)
                        hideLoading()

                    when (result) {
                        is CommonApiState.Success -> {
                            val bannerList = result.data
                            initBanner(bannerList)
                        }
                        is CommonApiState.Error -> showErrorMessage(result.message.toString())
                        is CommonApiState.Loading -> showLoading()
                        is CommonApiState.Init -> {}
                    }
                }
            }
        }
    }

    /**
     * viewModel.getMemberInfoResult 결과값 view 반영
     */
    private fun observeGetMemberInfoState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMemberInfoResult.collectLatest { result ->
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> {
                        val memberResult = result.data

                        getPreferencesControl().saveIntValue(Constants.SHARED_MEMBER_ID_VALUE, memberResult.memberId)
                        when(memberResult.petId) {
                            null -> {
                                binding.textViewMemberName.text = getString(R.string.home_no_petid)
                                setPetidCardType(null)
                            }
                            else -> {
                                memberResult.name.let {
                                    binding.textViewMemberName.text = it
                                    binding.textViewOwnerName.text = String.format(getString(R.string.to_owner), it)
                                }

                                binding.textViewAddress.text =
                                    listOf(memberResult.address, memberResult.addressDetails)
                                        .joinToString(", ")

                                memberResult.petId!!.toLong().let { petId ->
                                    viewModel.getPetDetails(petId)
                                    getPreferencesControl().saveIntValue(Constants.SHARED_PET_ID_VALUE, petId.toInt())
                                }
                                getPreferencesControl().saveIntValue(Constants.SHARED_MEMBER_ID_VALUE, memberResult.memberId)
                            }
                        }
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
                }
            }
        }
    }

    /**
     * viewModel.getPetDetails 결과값 view 반영
     */
    private fun observeGetPetInfoState() {
        lifecycleScope.launch {
            viewModel.getPetDetailsResult.collectLatest { result ->
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> {
                        with(result.data) {
                            setPetidCardType(chipType)
                            getPreferencesControl().saveStringValue(Constants.SHARED_PET_CHIP_TYPE, chipType)

                            binding.apply {
                                textViewPetNameBack.text = petName
                                textViewPetNameFront.text = petName
                                textViewType.text = appearance.breed

                                textViewAge.text = String.format(getString(R.string.to_age), calculateAge(petBirthDate))
                                textViewBirth.text = petBirthDate
                                textViewGender.text =
                                    listOf(genderCharToString(petSex.first()),
                                        String.format(getString(R.string.neutering),
                                            booleanCharToSign(petNeuteredYn[0])))
                                        .joinToString(", ")
                                textViewFeature.text =
                                    listOf(appearance.hairColor, appearance.hairLength).joinToString(", ")
                                textViewWeight.text =
                                    String.format(getString(R.string.to_kg), appearance.weight)
                            }
                        }
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
                }
            }
        }
    }

    /**
     * viewModel.getPetImageUrl 결과값 view 반영
     */
    private fun observeGetPetImageUrlState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPetImageUrlResult.collectLatest { result ->
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> {
                        R.color.d9.let {
                            Glide.with(requireContext())
                                .load(result.data)
                                .error(it)
                                .into(binding.imageViewCardPetPhoto)
                        }
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
                }
            }
        }
    }
}
