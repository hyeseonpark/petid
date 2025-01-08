package com.petid.petid.ui.view.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.petid.petid.BuildConfig
import com.petid.petid.R
import com.petid.petid.databinding.FragmentPetInfoInputBinding
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.util.showDatePicker
import com.petid.petid.util.throttleFirst
import com.petid.petid.viewmodel.generate.GeneratePetidSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.checkedChanges


@AndroidEntryPoint
class PetInfoInputFragment : BaseFragment<FragmentPetInfoInputBinding>(FragmentPetInfoInputBinding::inflate) {

    companion object{
        fun newInstance()= PetInfoInputFragment()
    }
    private val viewModel: GeneratePetidSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetInfoInputBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
        )
        initComponent()
    }

    fun initComponent() {
        with(binding) {
            // 생일 달력
            editTextBirth
                .clicks()
                .throttleFirst()
                .onEach {
                    showDatePicker(editTextBirth, requireContext())
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            // 중성화 날짜 달력
            editNeuteringDate
                .clicks()
                .throttleFirst()
                .onEach {
                    showDatePicker(editNeuteringDate, requireContext())
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            listOf(editTextName, editTextBirth, editNeuteringDate).forEach { editText ->
                editText.addTextChangedListener {
                    buttonNext.isEnabled = isPossibleToNextStep()
                }
            }

            // 중성화 여부에 따른 editNeuteringDate 입력 여부 변경
            checkboxIsNeutering
                .checkedChanges()
                .onEach {
                    editNeuteringDate.isEnabled = !it
                    if(it) editNeuteringDate.text = null
                    buttonNext.isEnabled = isPossibleToNextStep()
                }
                .launchIn(lifecycleScope)

            // 선택된 성별
            val checkedGender = when(radioButtonMale.isChecked) {
                true -> getString(R.string.male_initial)
                false -> getString(R.string.female_initial)
            }.first()

            // 중성화 여부
            val checkedNeutering = when(checkboxIsNeutering.isChecked) {
                true -> getString(R.string.N)
                false -> getString(R.string.Y)
            }.first()

            // 중성화 날짜, 중성화가 N 일 경우 null 을 반환
            val neuteringDate = when(editNeuteringDate.text.toString().isEmpty()) {
                true -> null
                false -> editNeuteringDate.text.toString()
            }

            buttonNext
                .clicks()
                .throttleFirst()
                .onEach {
                    viewModel.petInfo.setPetInfo(
                        editTextName.text.toString(),
                        editTextBirth.text.toString(),
                        checkedGender,
                        checkedNeutering,
                        neuteringDate
                    )
                    findNavController().navigate(R.id.action_petInfoInputFragment_to_petPhotoFragment)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            if (BuildConfig.DEBUG) {
                buttonNext.isEnabled = true
            }

        }
    }

    /**
     * 모든 항목이 입력됐는지 확인
     */
    private fun isPossibleToNextStep(): Boolean {
        return with(binding) {
            listOf(editTextName, editTextBirth).all { it.text.isNullOrEmpty().not() } &&
                    radioGroupGender.checkedRadioButtonId != -1 &&
                    checkboxIsNeutering.isChecked == editNeuteringDate.text.isNullOrEmpty()
        }
    }
}