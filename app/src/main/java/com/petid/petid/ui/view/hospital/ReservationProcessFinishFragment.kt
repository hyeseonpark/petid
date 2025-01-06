package com.petid.petid.ui.view.hospital

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.petid.petid.R
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.databinding.FragmentReservationProcessFinishBinding
import com.petid.petid.ui.component.CustomDialogCommon

class ReservationProcessFinishFragment: BaseFragment<FragmentReservationProcessFinishBinding>(
    FragmentReservationProcessFinishBinding::inflate) {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationProcessFinishBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
            onBackClick = { activity?.finish() },
        )
        initComponent()
    }

    fun initComponent() {

        // 뒤로가기 버튼 선택 시 Activity 종료
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        binding.buttonConfirm.setOnClickListener{
            requireActivity().finish()
        }

        // 알람권한, 13 이상만
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    getGlobalContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                CustomDialogCommon(
                    title = getString(R.string.hospital_process_finish_dialog_title),
                    isSingleButton = true,
                    singleButtonText = getString(R.string.hospital_process_finish_dialog_button),
                    yesButtonClick = {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }).show(this.childFragmentManager, "CustomDialogCommon")
            }
        }
    }
}