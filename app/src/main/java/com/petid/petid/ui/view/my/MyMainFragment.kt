package com.petid.petid.ui.view.my

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.petid.petid.R
import com.petid.petid.common.Constants
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.GlobalApplication.Companion.getPreferencesControl
import com.petid.petid.databinding.FragmentMyMainBinding
import com.petid.petid.ui.component.CustomDialogCommon
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.ui.view.auth.SocialAuthActivity
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.util.PreferencesControl
import com.petid.petid.util.showErrorMessage
import com.petid.petid.util.throttleFirst
import com.petid.petid.viewmodel.my.MyInfoViewModel
import com.bumptech.glide.Glide
import com.petid.petid.util.petidNullDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponent()
        viewModel.getMemberInfo()
        observeGetMemberInfoState()
        observeGetMemberImageState()
        observeDoWithdrawState()
    }

    private fun initComponent() {
        with(binding) {

            // 내 정보
            layoutMyinfo
                .clicks()
                .throttleFirst()
                .onEach {
                    when(petIdValue) {
                        -1 -> petidNullDialog(requireContext()).show(childFragmentManager, "petidNullDialog")
                        else -> {
                            val target = Intent(activity, MyInfoActivity::class.java)
                            startActivity(target)
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            // 반려동물 정보
            layoutPetInfo
                .clicks()
                .throttleFirst()
                .onEach {
                    when(petIdValue) {
                        -1 -> petidNullDialog(requireContext()).show(childFragmentManager, "petidNullDialog")
                        else -> {
                            val target = Intent(activity, PetInfoActivity::class.java)
                            startActivity(target)
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            // 예약 내역
            layoutReservationHistory
                .clicks()
                .throttleFirst()
                .onEach {
                    val target = Intent(activity, ReservationHistoryInfoActivity::class.java)
                    startActivity(target)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            // 약관 및 개인정보 처리 동의
            layoutTermsAgreeInfo
                .clicks()
                .throttleFirst()
                .onEach {

                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            // 개인정보 처리방침
            layoutPersonalServiceInfo
                .clicks()
                .throttleFirst()
                .onEach {

                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            // 공지사항
            layoutNotice
                .clicks()
                .throttleFirst()
                .onEach {

                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            // 자주하는 질문
            layoutQna
                .clicks()
                .throttleFirst()
                .onEach {

                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            // 탈퇴하기
            textViewWithdraw
                .clicks()
                .throttleFirst()
                .onEach {
                    showWithdrawDialog()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            // 로그아웃
            textViewLogout
                .clicks()
                .throttleFirst()
                .onEach {
                    doLogout()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

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

            textViewAppVersion.text = "${getString(R.string.app_version)}${packageInfo.versionName}"
        }
    }

    /**
     * 회원 탈퇴 dialog
     */
    private fun showWithdrawDialog() {
        val withdrawDialog = CustomDialogCommon(
            boldTitle = getString(R.string.withdraw_dialog_title),
            title = getString(R.string.withdraw_dialog_desc),
            yesButtonClick = {
                viewModel.doWithdraw()
            })

        withdrawDialog.show(this.childFragmentManager, "CustomDialogCommon")
    }

    /**
     * 로그아웃
     */
    private fun doLogout() {
        getPreferencesControl().apply {
            clear()
            saveBooleanValue(Constants.SHARED_VALUE_IS_FIRST_RUN, false)
        }
        val target = Intent(activity, SocialAuthActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(target)
        activity?.finish()
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

    /**
     * viewModel.getMemberImage 결과값 view 반영
     */
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

    /**
     * viewModel.doWithdraw 결과값 view 반영
     */
    private fun observeDoWithdrawState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.doWithdrawResult.collectLatest { result ->
                    if (result !is CommonApiState.Loading)
                        hideLoading()

                    when (result) {
                        is CommonApiState.Success -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.success_withdraw), Toast.LENGTH_LONG).show()
                            doLogout()
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
