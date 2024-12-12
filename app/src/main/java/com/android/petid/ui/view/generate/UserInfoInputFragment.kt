package com.android.petid.ui.view.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.android.petid.BuildConfig
import com.android.petid.R
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentUserInfoInputBinding
import com.android.petid.ui.view.common.BundleKeys
import com.android.petid.viewmodel.generate.GeneratePetidSharedViewModel

class UserInfoInputFragment: BaseFragment<FragmentUserInfoInputBinding>(FragmentUserInfoInputBinding::inflate) {

    companion object{
        fun newInstance()= UserInfoInputFragment()
    }

    private val viewModel: GeneratePetidSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInfoInputBinding.inflate(inflater)
        initComponent()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
        )

        val keyEditTextMap = mapOf(
            BundleKeys.KEY_ADDRESS to binding.editTextAddress,
            BundleKeys.KEY_ADDRESS_RRA to binding.editTextRra
        )

        keyEditTextMap.forEach { (key, editText) ->
            setFragmentResultListener(key) { _, bundle ->
                val result = bundle.getString(key)
                editText.setText(result)
            }
        }
    }

    fun initComponent() {
        with(binding) {
            if (BuildConfig.DEBUG) {
                buttonNext.isEnabled = true
            }

            listOf(editTextName, editTextPhone, editTextAddress, editTextAddressDetail,
                editTextRra, editTextRraDetail).forEach { editText ->
                editText.addTextChangedListener {
                    buttonNext.isEnabled = isPossibleToNextStep()
                }
            }

            editTextAddress.setOnClickListener {
                findNavController().navigate(R.id.action_userInfoInputFragment_to_addressSearchFragment)
            }

            editTextRra.setOnClickListener{
                val action = UserInfoInputFragmentDirections
                    .actionUserInfoInputFragmentToAddressSearchFragment(BundleKeys.KEY_ADDRESS_RRA)
                findNavController().navigate(action)
            }

            checkboxTermsAgree.setOnCheckedChangeListener { _, isChecked ->
                buttonNext.isEnabled = isPossibleToNextStep()

                listOf(editTextRra, editTextRraDetail).forEach { editText ->
                    editText.visibility = if(isChecked) View.GONE else View.VISIBLE
                }
            }

            buttonNext.setOnClickListener{
                val name = editTextName.text.toString()
                val address = editTextAddress.text.toString()
                val addressDetail = editTextAddressDetail.text.toString()
                val rra = editTextRra.text.toString()
                val rraDetail = editTextRraDetail.text.toString()
                val phone = editTextPhone.text.toString()

                viewModel.petInfo.setProposer(name, address, addressDetail, rra, rraDetail, phone)

                findNavController().navigate(R.id.action_userInfoInputFragment_to_petInfoInputFragment)
            }
        }
    }

    /**
     * 모든 항목이 입력 됐는지 확인
     */
    private fun isPossibleToNextStep(): Boolean {
        return with(binding) {
            listOf(editTextName, editTextPhone, editTextAddress, editTextAddressDetail)
                .all { it.text.isNullOrEmpty().not() } &&
                    (checkboxTermsAgree.isChecked ||
                            (editTextRra.text.isNullOrEmpty().not() &&
                                    editTextRraDetail.text.isNullOrEmpty().not()))
        }
    }
}