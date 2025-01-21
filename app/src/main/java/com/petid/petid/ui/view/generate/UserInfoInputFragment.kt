package com.petid.petid.ui.view.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.petid.petid.BuildConfig
import com.petid.petid.R
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.databinding.FragmentUserInfoInputBinding
import com.petid.petid.ui.view.common.BundleKeys
import com.petid.petid.util.addPhoneNumberFormatting
import com.petid.petid.util.throttleFirst
import com.petid.petid.viewmodel.generate.GeneratePetidSharedViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.checkedChanges

class UserInfoInputFragment: BaseFragment<FragmentUserInfoInputBinding>(FragmentUserInfoInputBinding::inflate) {
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
            listOf(editTextName, editTextPhone, editTextAddress, editTextAddressDetail,
                editTextRra, editTextRraDetail).forEach { editText ->
                editText.addTextChangedListener {
                    buttonNext.isEnabled = isPossibleToNextStep()
                }
            }

            editTextPhone.addPhoneNumberFormatting()

            editTextAddress
                .clicks()
                .throttleFirst()
                .onEach {
                    findNavController().navigate(R.id.action_userInfoInputFragment_to_addressSearchFragment)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            editTextRra
                .clicks()
                .throttleFirst()
                .onEach {
                    val action = UserInfoInputFragmentDirections
                        .actionUserInfoInputFragmentToAddressSearchFragment(BundleKeys.KEY_ADDRESS_RRA)
                    findNavController().navigate(action)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            checkboxTermsAgree
                .checkedChanges()
                .onEach { isChecked ->
                    buttonNext.isEnabled = isPossibleToNextStep()

                    listOf(editTextRra, editTextRraDetail).forEach { editText ->
                        editText.visibility = if(isChecked) View.GONE else View.VISIBLE
                    }
                }
                .launchIn(lifecycleScope)

            buttonNext
                .clicks()
                .throttleFirst()
                .onEach {
                    val name = editTextName.text.toString()
                    val address = editTextAddress.text.toString()
                    val addressDetail = editTextAddressDetail.text.toString()
                    val rra = editTextRra.text.toString()
                    val rraDetail = editTextRraDetail.text.toString()
                    val phone = editTextPhone.text.toString()

                    viewModel.petInfo.setProposer(name, address, addressDetail, rra, rraDetail, phone)

                    findNavController().navigate(R.id.action_userInfoInputFragment_to_petInfoInputFragment)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            if (BuildConfig.DEBUG) {
                buttonNext.isEnabled = true
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