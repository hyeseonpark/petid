package com.android.petid.ui.view.my

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.android.petid.R
import com.android.petid.common.Constants
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.databinding.FragmentMyMainBinding
import com.android.petid.ui.component.CustomDialogCommon
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.util.PreferencesControl
import com.android.petid.util.showErrorMessage
import com.android.petid.viewmodel.my.MyInfoViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 마이페이지 메인
 */
class MyMainFragment : BaseFragment<FragmentMyMainBinding>(FragmentMyMainBinding::inflate) {
    private val viewModel: MyInfoViewModel by activityViewModels()

    private val petIdValue =
        PreferencesControl(getGlobalContext()).getIntValue(Constants.SHARED_PET_ID_VALUE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyMainBinding.inflate(inflater)
        initComponent()
        viewModel.getMemberInfo()
        observeGetMemberInfoState()
        observeGetMemberImageState()
        return binding.root
    }

    private fun initComponent() {
        with(binding) {
            val petidNullDialog = CustomDialogCommon(getString(R.string.common_dialog_petid_null))

            // 내 정보
            layoutMyinfo.setOnClickListener {
                when(petIdValue) {
                    -1 -> petidNullDialog.show(childFragmentManager, null)
                    else -> {
                        val intent = Intent(activity, MyInfoActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            // 반려동물 정보
            layoutPetInfo.setOnClickListener {
                when(petIdValue) {
                    -1 -> petidNullDialog.show(childFragmentManager, null)
                    else -> {
                        val intent = Intent(activity, PetInfoActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            // 예약 내역
            layoutReservationHistory.setOnClickListener {
                val intent = Intent(activity, ReservationHistoryInfoActivity::class.java)
                startActivity(intent)
            }

            // 약관 및 개인정보 처리 동의
            layoutTermsAgreeInfo.setOnClickListener {
                //
            }

            // 개인정보 처리방침
            layoutPersonalServiceInfo.setOnClickListener {
                //
            }

            // 공지사항
            layoutNotice.setOnClickListener {
                //
            }

            // 자주하는 질문
            layoutQna.setOnClickListener {
                //
            }

            // 탈퇴하기
            layoutWithdraw.setOnClickListener {
                //
            }

            // 앱 버전
            val packageInfo: PackageInfo =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getGlobalContext().packageManager.getPackageInfo(
                        getGlobalContext().packageName,
                        PackageManager.PackageInfoFlags.of
                            (PackageManager.GET_SIGNING_CERTIFICATES.toLong()))
                } else {
                    getGlobalContext().packageManager.getPackageInfo(
                        getGlobalContext().packageName, PackageManager.GET_SIGNATURES)
                }

            textViewAppVersion.text = getString(R.string.app_version) + packageInfo.versionName
        }
    }


    /**
     * viewModel.getMemberInfoResult 결과값 view 반영
     */
    private fun observeGetMemberInfoState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getMemberInfoResult.collectLatest { result ->
                    if (result !is CommonApiState.Loading)
                        hideLoading()

                    when (result) {
                        is CommonApiState.Success -> {
                            with(result.data) {
                                binding.apply {
                                    textViewUserName.text = name
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
    }
    private fun observeGetMemberImageState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getMemberImageResult.collectLatest { result ->
                    if (result !is CommonApiState.Loading)
                        hideLoading()

                    when (result) {
                        is CommonApiState.Success -> {
                            R.drawable.ic_mypage_icon.let {
                                Glide
                                    .with(requireContext())
                                    .load(result.data)
                                    .placeholder(it)
                                    .error(it)
                                    .into(binding.imageViewProfile)
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
}