package com.android.petid.ui.view.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.android.domain.entity.BannerEntity
import com.android.petid.common.Constants.BANNER_TYPE_MAIN
import com.android.petid.databinding.FragmentHomeMainBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.generate.PetIdStartActivity
import com.android.petid.ui.view.home.adapter.HomeBannerAdapter
import com.android.petid.viewmodel.home.HomeMainVIewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeMainFragment : Fragment() {
    lateinit var binding: FragmentHomeMainBinding
    private val viewModel: HomeMainVIewModel by activityViewModels()

    private val TAG = "HomeMainFragment"

    // 배너
    private var bannerPosition = 0
    private var homeBannerHandler = HomeBannerHandler()
    // 배너 시간
    private val intervalTime = 5000.toLong()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeMainBinding.inflate(inflater)

        setupBannerObservers()

        binding.buttonCreateStart.button.setOnClickListener{
            val intent = Intent(activity, PetIdStartActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBannerList(BANNER_TYPE_MAIN)
    }

    private fun initBanner(bannerList: List<BannerEntity>) {

        val bannerAdapter = activity?.let { HomeBannerAdapter(bannerList, it) }
        bannerAdapter?.notifyDataSetChanged()

        // bannerList의 총 개수를 binding.textViewTotalPage.text에 설정
        val total = bannerList.size
        binding.textViewTotalPage.text = total.toString()

        binding.viewPagerBanner1.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPagerBanner1.adapter = bannerAdapter
        binding.viewPagerBanner1.setCurrentItem(bannerPosition, false) //시작 위치 지정

        binding.viewPagerBanner1.apply {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // 현재 포지션을 이용해 현재 페이지를 설정
                    val currentPage = position % total + 1
                    binding.textViewCurrentPage.text = currentPage.toString()
                }
                //이 메서드의 state 값으로 뷰페이저의 상태를 알 수 있음
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state) {
                        //뷰페이저가 움직이는 중일 때 자동 스크롤 시작 함수 호출
                        ViewPager2.SCROLL_STATE_DRAGGING -> autoScrollStop()
                        //뷰페이저에서 손 뗐을 때, 뷰페이저가 멈춰있을 때 자동 스크롤 멈춤 함수 호출
                        ViewPager2.SCROLL_STATE_IDLE -> autoScrollStart(intervalTime)
                    }
                }
            })
        }
    }

    //배너 자동 스크롤 시작하게 하는 함수
    private fun autoScrollStart(intervalTime: Long){
        homeBannerHandler.removeMessages(0) //이거 안하면 핸들러가 여러개로 계속 늘어남
        homeBannerHandler.sendEmptyMessageDelayed(0, intervalTime) //intervalTime만큼 반복해서 핸들러를 실행
    }

    //배너 자동 스크롤 멈추게 하는 함수
    private fun autoScrollStop(){
        homeBannerHandler.removeMessages(0) //핸들러 중지
    }

    //배너 자동 스크롤 컨트롤하는 클래스
    private inner class HomeBannerHandler: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == 0){
                binding.viewPagerBanner1.setCurrentItem(++bannerPosition, true) //다음 페이지로 이동
                autoScrollStart(intervalTime) //스크롤 킵고잉
            }
        }
    }

    /**
     * banner api observer
     */
    private fun setupBannerObservers() {
        lifecycleScope.launch {
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
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        autoScrollStart(intervalTime)
    }

    override fun onPause() {
        super.onPause()
        autoScrollStop()
    }
}
