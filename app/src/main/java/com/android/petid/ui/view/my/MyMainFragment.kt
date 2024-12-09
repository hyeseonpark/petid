package com.android.petid.ui.view.my

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petid.R
import com.android.petid.common.Constants
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentMyMainBinding
import com.android.petid.ui.component.CustomDialogCommon
import com.android.petid.ui.view.hospital.HospitalMainFragment
import com.android.petid.util.PreferencesControl

/**
 * 마이페이지 메인
 */
class MyMainFragment : BaseFragment<FragmentMyMainBinding>(FragmentMyMainBinding::inflate) {

    private val petIdValue =
        PreferencesControl(getGlobalContext()).getIntValue(Constants.SHARED_PET_ID_VALUE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyMainBinding.inflate(inflater)
        initComponent()
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
            layoutPetInfo.layout.setOnClickListener {
                when(petIdValue) {
                    -1 -> petidNullDialog.show(childFragmentManager, null)
                    else -> {
                        val intent = Intent(activity, PetInfoActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            // 예약 내역
            layoutReservationHistory.layout.setOnClickListener {
                val intent = Intent(activity, ReservationHistoryInfoActivity::class.java)
                startActivity(intent)
            }

            // 약관 및 개인정보 처리 동의
            layoutTermsAgreeInfo.layout.setOnClickListener {
                //
            }

            // 개인정보 처리방침
            layoutPersonalServiceInfo.layout.setOnClickListener {
                //
            }

            // 공지사항
            layoutNotice.layout.setOnClickListener {
                //
            }

            // 자주하는 질문
            layoutQna.layout.setOnClickListener {
                //
            }

            // 탈퇴하기
            layoutWithdraw.layout.setOnClickListener {
                //
            }
        }
    }
}