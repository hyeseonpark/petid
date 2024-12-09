package com.android.petid.ui.view.generate

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.databinding.FragmentScannedInfoBinding
import com.android.petid.ui.component.CustomDialogCommon
import com.android.petid.viewmodel.generate.GeneratePetidSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScannedInfoFragment : BaseFragment<FragmentScannedInfoBinding>(FragmentScannedInfoBinding::inflate) {

    private val viewModel: GeneratePetidSharedViewModel by activityViewModels()

    private lateinit var backPressedCallback: OnBackPressedCallback

    private lateinit var dialog : CustomDialogCommon

    override fun onAttach(context: Context) {
        super.onAttach(context)

        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialog.show(childFragmentManager, "CustomDialogCommon")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScannedInfoBinding.inflate(layoutInflater)
        initComponent()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val appearance = viewModel.petInfo.getAppearance()
        if (appearance != null) {
            with(appearance) {
                binding.editTextType.editTextText.setText(breed)
                binding.editTextHairColor.editTextText.setText(hairColor)
                binding.editTextHairFeature.editTextText.setText(hairLength)
                binding.editTextWeight.editTextText.setText(weight.toString())
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initComponent() {
        // 우측 아이콘 클릭 이벤트
        with(binding) {
            listOf(editTextType, editTextHairColor, editTextHairFeature, editTextWeight).forEach { editText ->
                with(editText.editTextText) {
                    setOnTouchListener { v, event ->
                        if (event.action == MotionEvent.ACTION_UP) {
                            // 오른쪽 drawable
                            compoundDrawables[2]?.let { drawableRight ->
                                val drawableWidth = drawableRight.bounds.width()
                                val clickAreaStartX = width - paddingEnd - drawableWidth

                                // 터치 위치가 오른쪽 drawable 영역 내에 있을 경우 true
                                if (event.x >= clickAreaStartX) {
                                    text.clear()
                                    clearFocus()
                                    return@setOnTouchListener true
                                }
                            }
                        }
                        false
                    }
                }
            }

            buttonNext.setOnClickListener{
                viewModel.petInfo.setAppearance(
                    editTextType.editTextText.text.toString(),
                    editTextHairColor.editTextText.text.toString(),
                    editTextWeight.editTextText.text.toString().toInt(),
                    editTextHairFeature.editTextText.text.toString(),
                )
                findNavController().navigate(R.id.action_scannedInfoFragment_to_checkingInfoFragment)
            }

            dialog = CustomDialogCommon(
                getString(R.string.petid_generate_step_5_dialog), {
                    findNavController().popBackStack()
                })
        }
    }
}