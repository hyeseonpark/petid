package com.android.petid.ui.view.my

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.petid.databinding.FragmentMyMainBinding
import com.android.petid.ui.view.generate.PetInfoInputActivity

/**
 * 마이페이지 메인
 */
class MyMainFragment : Fragment() {
    lateinit var binding: FragmentMyMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyMainBinding.inflate(inflater)
        initComponent()
        return binding.root
    }

    fun newInstant() : MyMainFragment
    {
        val args = Bundle()
        val frag = MyMainFragment()
        frag.arguments = args
        return frag
    }

    private fun initComponent() {
        // 내 정보
        binding.layoutMyinfo.setOnClickListener {
            val intent = Intent(activity, MyInfoActivity::class.java)
            startActivity(intent)
        }

        // 반려동물 정보
        binding.layoutPetInfo.layout.setOnClickListener {
            val intent = Intent(activity, MyPetInfoActivity::class.java)
            startActivity(intent)
        }

        // 예약 내역
        binding.layoutReservationHistory.layout.setOnClickListener {
            val intent = Intent(activity, ReservationHistoryInfoActivity::class.java)
            startActivity(intent)
        }

        // 약관 및 개인정보 처리 동의
        binding.layoutTermsAgreeInfo.layout.setOnClickListener {
            //
        }

        // 개인정보 처리방침
        binding.layoutPersonalServiceInfo.layout.setOnClickListener {
            //
        }

        // 공지사항
        binding.layoutNotice.layout.setOnClickListener {
            //
        }

        // 자주하는 질문
        binding.layoutQna.layout.setOnClickListener {
            //
        }

        // 탈퇴하기
        binding.layoutWithdraw.layout.setOnClickListener {
            //
        }
    }
}