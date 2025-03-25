package com.petid.petid.ui.view.hospital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.petid.petid.R
import com.petid.petid.databinding.FragmentHospitalDetailBinding
import com.petid.petid.ui.component.CustomDialogCommon
import com.petid.petid.ui.state.CommonUIState
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.util.collectLatestFlow
import com.petid.petid.util.showErrorMessage
import com.petid.petid.util.throttleFirst
import com.petid.petid.viewmodel.hospital.HospitalViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

class HospitalDetailFragment: BaseFragment<FragmentHospitalDetailBinding>(FragmentHospitalDetailBinding::inflate) {
    private val viewModel: HospitalViewModel by activityViewModels()

    private lateinit var infoDialog : CustomDialogCommon

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHospitalDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
            onBackClick = { requireActivity().finish() },
        )
        initComponent()
        observeHospitalDetail()
    }

    private fun initComponent() {
        with(binding) {
            infoDialog = CustomDialogCommon(
                title = getString(R.string.hospital_make_reservation_dialog_info),
                yesButtonClick = {
                    findNavController().navigate(
                        R.id.action_hospitalDetailFragment_to_reservationCalendarFragment) },
                isSingleButton = true,
                singleButtonText = getString(R.string.hospital_make_reservation_dialog_info_button))

            buttonReserve
                .clicks()
                .throttleFirst()
                .onEach {
                    findNavController().navigate(
                        R.id.action_hospitalDetailFragment_to_reservationCalendarFragment)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    /**
     *  hospital detail observe
     */
    private fun observeHospitalDetail() {
        viewModel.hospitalDetailApiState.collectLatestFlow(this) {result ->
            if (result !is CommonUIState.Loading)
                hideLoading()

            when (result) {
                is CommonUIState.Success -> {
                    with(binding) {
                        val resultData = result.data

                        // 상태바
                        setupTitle(resultData.name)

                        // 이미지
                        (R.drawable.img_hospital_list_empty).let {
                            val imgSource: Any? = when(resultData.imageUrl.isEmpty()) {
                                true -> AppCompatResources.getDrawable(requireContext(), it)
                                false -> resultData.imageUrl.first()
                            }

                            Glide.with(requireContext())
                                .load(imgSource)
                                .placeholder(it)
                                .error(it)
                                .into(imageViewHospitalPhoto)
                        }

                        resultData.run {
                            textViewTitle.text = name
                            textViewVet.text = vet
                            textViewTime.text = hours
                            textViewPlace.text = address
                            textViewTel.text = tel
                        }
                    }
                }
                is CommonUIState.Error -> showErrorMessage(result.message.toString())
                is CommonUIState.Loading -> showLoading()
                is CommonUIState.Init -> {}
            }
        }
    }
}