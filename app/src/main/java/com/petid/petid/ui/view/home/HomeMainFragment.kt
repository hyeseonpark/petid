package com.petid.petid.ui.view.home

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
import com.bumptech.glide.Glide
import com.petid.domain.entity.BannerEntity
import com.petid.petid.BuildConfig
import com.petid.petid.R
import com.petid.petid.common.Constants
import com.petid.petid.common.Constants.BANNER_TYPE_MAIN
import com.petid.petid.common.Constants.CHIP_TYPE
import com.petid.petid.GlobalApplication.Companion.getPreferencesControl
import com.petid.petid.databinding.FragmentHomeMainBinding
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.ui.view.generate.GeneratePetidMainActivity
import com.petid.petid.ui.view.home.adapter.HomeBannerAdapter
import com.petid.petid.util.booleanCharToSign
import com.petid.petid.util.calculateAge
import com.petid.petid.util.genderCharToString
import com.petid.petid.util.showErrorMessage
import com.petid.petid.util.throttleFirst
import com.petid.petid.viewmodel.home.HomeMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks


@AndroidEntryPoint
class HomeMainFragment : BaseFragment<FragmentHomeMainBinding>(FragmentHomeMainBinding::inflate) {

    private val viewModel: HomeMainViewModel by activityViewModels()

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
        observeHasUncheckedNotificationState()

        viewModel.getBannerList(BANNER_TYPE_MAIN)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMemberInfo()
        viewModel.hasUncheckedNotification()
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
    fun setPetidCardType(type: String?) {
        with(binding) {
            when(type) {
                CHIP_TYPE[0], CHIP_TYPE[1] -> {
                    viewNoPetidCard.visibility = View.GONE
                    viewPetidCard.visibility = View.VISIBLE
                    layoutRegister.visibility = View.VISIBLE
                    textViewFlip.visibility = View.VISIBLE
                }
                CHIP_TYPE[2] -> {
                    viewNoPetidCard.visibility = View.GONE
                    viewPetidCard.visibility = View.VISIBLE
                    layoutRegister.visibility = View.GONE
                    textViewFlip.visibility = View.VISIBLE
                }
                null -> {
                    viewNoPetidCard.visibility = View.VISIBLE
                    viewPetidCard.visibility = View.GONE
                    textViewFlip.visibility = View.GONE
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
                viewModel.bannerScrollPosition.collect { position ->
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
                viewModel.bannerApiState.collect { result ->
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
            viewModel.getMemberInfoResult.collect { result ->
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
            viewModel.getPetDetailsResult.collect { result ->
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
            viewModel.getPetImageUrlResult.collect { result ->
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when (result) {
                    is CommonApiState.Success -> {
                        // TODO 강사님께 금요일날 확인받기
                        Glide.with(requireContext())
                            .load(result.data)
                            .error(R.color.d9)
                            .into(binding.imageViewCardPetPhoto)
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
    private fun observeHasUncheckedNotificationState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.unchekedNotifcationState.collect { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        val image = when(result.data) {
                            true -> R.drawable.ic_home_noti_alert
                            false -> R.drawable.ic_home_noti_default
                        }

                        binding.imageViewNoti.setImageResource(image)
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> {}
                    is CommonApiState.Init -> {}
                }
            }
        }
    }
}
