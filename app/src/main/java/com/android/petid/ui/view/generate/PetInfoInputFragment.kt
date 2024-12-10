package com.android.petid.ui.view.generate

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.petid.BuildConfig
import com.android.petid.R
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentPetInfoInputBinding
import com.android.petid.util.Utils.getCurrentDate
import com.android.petid.viewmodel.generate.GeneratePetidSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


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
        initComponent()

        return binding.root
    }

    fun initComponent() {
        with(binding) {
            if (BuildConfig.DEBUG) {
                buttonNext.isEnabled = true
            }

            // 생일 달력
            editTextBirth.setOnClickListener {
                showDatePicker(editTextBirth)
            }

            // 중성화 날짜 달력
            editNeuteringDate.setOnClickListener {
                showDatePicker(editNeuteringDate)
            }

            listOf(editTextName, editTextBirth, editNeuteringDate).forEach { editText ->
                editText.addTextChangedListener {
                    buttonNext.isEnabled = isPossibleToNextStep()
                }
            }

            // 중성화 여부에 따른 editNeuteringDate 입력 여부 변경
            checkboxIsNeutering.setOnCheckedChangeListener { button, _ ->
                editNeuteringDate.isEnabled = !button.isChecked
                if(button.isChecked) editNeuteringDate.text = null
                buttonNext.isEnabled = isPossibleToNextStep()
            }

            buttonNext.setOnClickListener{
                viewModel.petInfo.setPetInfo(
                    editTextName.text.toString(),
                    editTextBirth.text.toString(),
                    if(radioButtonMale.isChecked) 'M' else 'W',
                    if(checkboxIsNeutering.isChecked) 'N' else 'Y',
                    editNeuteringDate.text.toString()
                )
                findNavController().navigate(R.id.action_petInfoInputFragment_to_petPhotoFragment)
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

    /**
     * 달력 표시
     */
    private fun showDatePicker(editText: EditText) {
        val currentDate = editText.text.toString().ifEmpty { getCurrentDate() }
        val dateParts = currentDate.split("-")

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DatePickerDialogTheme,
            { _, year, monthOfYear, dayOfMonth ->
                editText.setText(
                    String.format(Locale.KOREA, "%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth)
                )
            },
            dateParts[0].toInt(),
            dateParts[1].toInt() - 1,
            dateParts[2].toInt()
        )
        datePickerDialog.apply {
            show()
            getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
                resources.getColor(R.color.petid_clear_blue, null))
            getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
                resources.getColor(R.color.petid_subtitle, null))
        }
    }

}