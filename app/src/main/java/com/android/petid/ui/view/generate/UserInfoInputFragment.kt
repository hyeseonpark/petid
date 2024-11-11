package com.android.petid.ui.view.generate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.android.petid.BuildConfig
import com.android.petid.R
import com.android.petid.databinding.FragmentUserInfoInputBinding
import com.android.petid.ui.view.common.BundleKeys
import com.android.petid.viewmodel.generate.GeneratePetidSharedViewModel

class UserInfoInputFragment : Fragment() {
    private lateinit var binding: FragmentUserInfoInputBinding
    private val viewModel: GeneratePetidSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfoInputBinding.inflate(inflater)
        initComponent()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(BundleKeys.KEY_ADDRESS) { _, bundle ->
            val result = bundle.getString(BundleKeys.KEY_ADDRESS)
            binding.editTextAddress.setText(result)
        }
    }

    fun initComponent() {
        with(binding) {
            if (BuildConfig.DEBUG) {
                buttonNext.isEnabled = true
            }

            listOf(editTextName, editTextPhone, editTextAddress, editTextAddressDetail).forEach { editText ->
                editText.addTextChangedListener {
                    buttonNext.isEnabled = isPossibleToNextStep()
                }
            }

            editTextAddress.setOnClickListener {
                findNavController().navigate(R.id.action_userInfoInputFragment_to_addressSearchFragment)
            }

            buttonNext.setOnClickListener{
                /*viewModel.memberInfo.apply {
                    name = editTextName.text.toString()
                }
                val regidencyValue = editTextResidencyNumber.text.toString() + editTextResidencyNumber2.text.toString()*/

                val nameValue = editTextName.text.toString()
                val phoneValue = editTextPhone.text.toString()
                val addressValue = editTextAddress.text.toString()
                val addressDetailValue = editTextAddressDetail.text.toString()

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
                .all { it.text.isNullOrEmpty().not() }
        }
    }
}