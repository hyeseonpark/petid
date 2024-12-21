package com.android.petid.ui.view.generate

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.databinding.FragmentScannedInfoBinding
import com.android.petid.ui.component.CustomDialogCommon
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.viewmodel.generate.GeneratePetidSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScannedInfoFragment : BaseFragment<FragmentScannedInfoBinding>(FragmentScannedInfoBinding::inflate) {

    private val viewModel: GeneratePetidSharedViewModel by activityViewModels()
    private lateinit var dialog : CustomDialogCommon

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialog.show(childFragmentManager, null)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScannedInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
            onBackClick = { dialog.show(childFragmentManager, null) }
        )
        initComponent()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun initComponent() {
        // 우측 아이콘 클릭 이벤트
        with(binding) {
            listOf(autoCompleteTextViewBreed, autoCompleteTextViewColor,
                autoCompleteTextViewFeature, editTextWeight).forEach { editText ->
                with(editText) {
                    setOnTouchListener { v, event ->
                        if (event.action == MotionEvent.ACTION_UP) {
                            // 오른쪽 drawable
                            compoundDrawables[2]?.let { drawableRight ->
                                val drawableWidth = drawableRight.bounds.width()
                                val clickAreaStartX = width - paddingEnd - drawableWidth

                                // 터치 위치가 오른쪽 drawable 영역 내에 있을 경우 true
                                if (event.x >= clickAreaStartX) {
                                    text?.clear()
                                    clearFocus()
                                    return@setOnTouchListener true
                                }
                            }
                        } else {
                            (editText as? AutoCompleteTextView)?.showDropDown()
                        }
                        false
                    }
                }
            }

            setAutoCompleteAdapter(autoCompleteTextViewBreed, R.array.type_dog_breeds)
            setAutoCompleteAdapter(autoCompleteTextViewColor, R.array.type_dog_colors)
            setAutoCompleteAdapter(autoCompleteTextViewFeature, R.array.type_dog_hair_length)

            val appearance = viewModel.petInfo.getAppearance()
            appearance?.also {
                autoCompleteTextViewBreed.setText(it.breed)
                autoCompleteTextViewColor.setText(it.hairColor)
                autoCompleteTextViewFeature.setText(it.hairLength)
                editTextWeight.setText("${it.weight}")
            }

            buttonNext.setOnClickListener{
                viewModel.petInfo.setAppearance(
                    autoCompleteTextViewBreed.text.toString(),
                    autoCompleteTextViewColor.text.toString(),
                    editTextWeight.text.toString().toIntOrNull() ?: 0,
                    autoCompleteTextViewFeature.text.toString(),
                )
                findNavController().navigate(R.id.action_scannedInfoFragment_to_checkingInfoFragment)
            }

            dialog = CustomDialogCommon(
                getString(R.string.petid_generate_step_5_dialog), {
                    findNavController().popBackStack()
                })
        }
    }

    /**
     *  set adapter for AutoCompleteTextView
     */
    fun setAutoCompleteAdapter(autoCompleteTextView: AutoCompleteTextView, arrayResId: Int) {
        val adapter = object : ArrayAdapter<String>(
            getGlobalContext(),
            android.R.layout.simple_dropdown_item_1line,
            resources.getStringArray(arrayResId)
        ) {
            override fun getView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getView(position, convertView, parent)
                view.setBackgroundColor(Color.WHITE)
                return view
            }
        }
        autoCompleteTextView.setAdapter(adapter)
    }
}