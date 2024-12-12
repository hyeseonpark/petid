package com.android.petid.ui.view.my

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentMyInfoUpdateBinding
import com.android.petid.ui.component.CustomDialogCommon
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.common.BundleKeys
import com.android.petid.viewmodel.my.MyInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 마이페이지 메인 > 내 정보 > 내 정보 수정
 */
@AndroidEntryPoint
class MyInfoUpdateFragment
    : BaseFragment<FragmentMyInfoUpdateBinding>(FragmentMyInfoUpdateBinding::inflate) {

    private val viewModel: MyInfoViewModel by activityViewModels()
    private val TAG = "MyInfoUpdateFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyInfoUpdateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
            title = getString(R.string.update_my_info_title),
        )

        // 주소 검색 후 결과값
        setFragmentResultListener(BundleKeys.KEY_ADDRESS) { _, bundle ->
            val result = bundle.getString(BundleKeys.KEY_ADDRESS)
            binding.editTextAddress.setText(result)
        }
        initComponent()
        observeGetMemberInfoState()
        observeUpdateMemberInfoState()
    }                                                                  

    private fun initComponent() {
        with(binding) {
            buttonComplete.setOnClickListener {
                completeDialog()
            }
            editTextAddress.setOnClickListener {
                findNavController().navigate(R.id.action_myInfoUpdateFragment_to_addressSearchFragment)
            }
        }
    }

    /**
     * viewModel.getMemberInfoResult 결과값 view 반영
     */
    private fun observeGetMemberInfoState() {
        lifecycleScope.launch {
            viewModel.getMemberInfoResult.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        with(result.data) {
                            binding.editTextName.setText(name)
                            binding.editTextPhone.setText(phone)
                            binding.editTextAddress.setText(address)
                            binding.editTextAddressDetail.setText(addressDetails)
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
     * my info update dialog
     */
    private fun completeDialog() {
        val dialog = CustomDialogCommon(
            getString(R.string.update_complete_dialog), {
                with(binding) {
                    viewModel.updateMemberInfo(
                        address = editTextAddress.text.toString(),
                        addressDetails = editTextAddressDetail.text.toString(),
                        phone = editTextPhone.text.toString()
                    )
                }
            })

        dialog.show(this.childFragmentManager, "CustomDialogCommon")
    }

    /**
     * viewModel.updateMemberInfoResult: 업데이트 완료시 뒤로가기
     */
    private fun observeUpdateMemberInfoState() {
        lifecycleScope.launch {
            viewModel.updateMemberInfoResult.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
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