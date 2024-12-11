package com.android.petid.ui.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.annotation.NonNull
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
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
import com.android.petid.util.Utils.booleanCharToSign
import com.android.petid.util.Utils.genderCharToString
import com.android.petid.viewmodel.home.HomeMainVIewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeMainFragment : BaseFragment<FragmentHomeMainBinding>(FragmentHomeMainBinding::inflate) {

    private val viewModel: HomeMainVIewModel by activityViewModels()

    private val TAG = "HomeMainFragment"

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

        viewModel.getBannerList(BANNER_TYPE_MAIN)
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
                findNavController().navigate(R.id.action_homeMainFragment_to_notificationFragment)
            }

            buttonCreateStart.setOnClickListener{
                val intent = Intent(activity, GeneratePetidMainActivity::class.java)
                startActivity(intent)
            }

            layoutRegister.setOnClickListener {
                val intent = Intent(activity, GeneratePetidMainActivity::class.java)
                startActivity(intent)
            }

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

            // Debug 상태에선 로고 클릭시 카드 타입 변경 가능
            if(BuildConfig.DEBUG) {
                imageViewLogo.setOnClickListener {
                    when(binding.viewNoPetidCard.visibility) {
                        View.VISIBLE -> setPetidCardType(CHIP_TYPE[1])
                        View.GONE -> setPetidCardType(CHIP_TYPE[0])
                    }
                }
            }
        }
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
     * 배너 초기화
     */
    private fun initBanner(bannerList: List<BannerEntity>) {
        bannerAdapter.submitList(bannerList)

        // bannerList의 총 개수를 binding.textViewTotalPage.text에 설정
        val total = bannerList.size
        binding.textViewTotalPage.text = total.toString()

        // 자동 스크롤 시작
        viewModel.startAutoScroll()
    }

    /**
     *
     */
    class SnapPagerScrollListener(// Properties
        private val snapHelper: PagerSnapHelper,
        @Type private val type: Int,
        private val notifyOnInit: Boolean,
        private val listener: OnChangeListener
    ) :
        RecyclerView.OnScrollListener() {
        @IntDef(*[ON_SCROLL, ON_SETTLED])
        annotation class Type

        interface OnChangeListener {
            fun onSnapped(position: Int)
        }

        private var snapPosition: Int

        // Constructor
        init {
            this.snapPosition = RecyclerView.NO_POSITION
        }

        // Methods
        override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if ((type == ON_SCROLL) || !hasItemPosition()) {
                notifyListenerIfNeeded(getSnapPosition(recyclerView))
            }
        }

        override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (type == ON_SETTLED && newState == RecyclerView.SCROLL_STATE_IDLE) {
                notifyListenerIfNeeded(getSnapPosition(recyclerView))
            }
        }

        private fun getSnapPosition(recyclerView: RecyclerView): Int {
            val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION

            val snapView = snapHelper.findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION

            return layoutManager.getPosition(snapView)
        }

        private fun notifyListenerIfNeeded(newSnapPosition: Int) {
            if (snapPosition != newSnapPosition) {
                if (notifyOnInit && !hasItemPosition()) {
                    listener.onSnapped(newSnapPosition)
                } else if (hasItemPosition()) {
                    listener.onSnapped(newSnapPosition)
                }

                snapPosition = newSnapPosition
            }
        }

        private fun hasItemPosition(): Boolean {
            return snapPosition != RecyclerView.NO_POSITION
        }

        companion object {
            // Constants
            const val ON_SCROLL: Int = 0
            const val ON_SETTLED: Int = 1
        }
    }

    /**
     * banner api observer
     */
    private fun setupBannerObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bannerApiState.collectLatest { result ->
                    when (result) {
                        is CommonApiState.Success -> {
                            val bannerList = result.data
                            initBanner(bannerList)
                        }

                        is CommonApiState.Error -> {
                            Log.e(TAG, "${result.message}")
                        }

                        is CommonApiState.Loading -> {
                            Log.d(TAG, "Loading....................")
                        }

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
                when (result) {
                    is CommonApiState.Success -> {
                        val memberResult = result.data
                        when(memberResult.petId) {
                            null -> {
                                binding.textViewMemberName.text = getString(R.string.home_no_petid)
                                setPetidCardType(CHIP_TYPE[0])
                            }
                            else -> {
                                memberResult.name.let {
                                    binding.textViewMemberName.text = it
                                    binding.textViewOwnerName.text = String.format(getString(R.string.to_owner), it)
                                }

                                memberResult.petId!!.toLong().let { petId ->
                                    viewModel.getPetDetails(petId)
                                    getPreferencesControl().saveIntValue(Constants.SHARED_PET_ID_VALUE, petId.toInt())
                                }
                                getPreferencesControl().saveIntValue(Constants.SHARED_MEMBER_ID_VALUE, memberResult.memberId)
                            }
                        }
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                    is CommonApiState.Init -> {}
                }
            }
        }
    }

    /**
     * @param type CHIP_TYPE 에 따른 펫아이디 카드
     */
    private fun setPetidCardType(type: String) {
        with(binding) {
            when(type) {
                CHIP_TYPE[0] -> {
                    viewNoPetidCard.visibility = View.VISIBLE
                    viewPetidCard.visibility = View.GONE
                }
                CHIP_TYPE[1] -> {
                    viewNoPetidCard.visibility = View.GONE
                    viewPetidCard.visibility = View.VISIBLE
                    layoutRegister.visibility = View.VISIBLE
                }
                CHIP_TYPE[2] -> {
                    viewNoPetidCard.visibility = View.GONE
                    viewPetidCard.visibility = View.VISIBLE
                    layoutRegister.visibility = View.GONE
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
                when (result) {
                    is CommonApiState.Success -> {
                        with(result.data) {
                            setPetidCardType(chipType)
                            /*petImages?.takeIf { it.isNotBlank() }?.let {
                                com.bumptech.glide.Glide.with(requireContext()).load(it).into(binding.imageViewProfile)
                            }*/
                            binding.apply {
                                textViewPetNameBack.text = petName
                                textViewPetNameFront.text = petName
                                textViewType.text = appearance.breed

                                textViewAge.text = String.format(getString(R.string.to_age), petBirthDate)
                                textViewBirth.text = petBirthDate
                                textViewGender.text =
                                    listOf(genderCharToString(petSex[0]), // TODO API 수정되면 수정
                                        String.format(getString(R.string.neutering),
                                            booleanCharToSign(petNeuteredYn[0])))
                                        .joinToString(", ")
                                textViewWeight.text =
                                    String.format(getString(R.string.to_kg), appearance.weight)
                                textViewFeature.text =
                                    listOf(appearance.hairColor, appearance.hairLength).joinToString(", ")
                            }
                        }
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                    is CommonApiState.Init -> {}
                }
            }
        }
    }
}
