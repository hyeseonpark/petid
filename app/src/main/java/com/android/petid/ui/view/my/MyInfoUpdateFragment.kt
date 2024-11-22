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
import com.android.petid.common.BaseFragment
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

    companion object{
        fun newInstance()= MyInfoUpdateFragment()
    }

    private val viewModel: MyInfoViewModel by activityViewModels()
    private val TAG = "MyInfoUpdateFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyInfoUpdateBinding.inflate(inflater)
        initComponent()
        observeGetMemberInfoState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 주소 검색 후 결과값
        setFragmentResultListener(BundleKeys.KEY_ADDRESS) { _, bundle ->
            val result = bundle.getString(BundleKeys.KEY_ADDRESS)
            binding.editTextAddress.setText(result)
        }
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
                requireActivity().onBackPressedDispatcher.onBackPressed()
            })

        dialog.show(this.childFragmentManager, "CustomDialogCommon")
    }
}