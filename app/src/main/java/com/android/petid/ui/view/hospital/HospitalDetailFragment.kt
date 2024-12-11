package com.android.petid.ui.view.hospital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.common.Constants
import com.android.petid.common.GlobalApplication.Companion.getPreferencesControl
import com.android.petid.databinding.FragmentHospitalDetailBinding
import com.android.petid.ui.component.CustomDialogCommon
import com.android.petid.ui.view.common.BaseFragment
import com.android.petid.viewmodel.hospital.HospitalViewModel
import com.bumptech.glide.Glide

class HospitalDetailFragment: BaseFragment<FragmentHospitalDetailBinding>(FragmentHospitalDetailBinding::inflate) {
    private val viewModel: HospitalViewModel by activityViewModels()

    private val TAG = "HospitalDetailFragment"

    private lateinit var infoDialog : CustomDialogCommon
    private lateinit var petidNullDialog : CustomDialogCommon

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHospitalDetailBinding.inflate(layoutInflater)

        initComponent()

        return binding.root

    }

    private fun initComponent() {
        with(binding) {
            infoDialog = CustomDialogCommon(
                title = getString(R.string.hospital_make_reservation_dialog_info_button),
                yesButtonClick = {
                    findNavController().navigate(
                        R.id.action_hospitalDetailFragment_to_reservationCalendarFragment) },
                isSingleButton = true,
                singleButtonText = getString(R.string.hospital_make_reservation_dialog_info_button))

            petidNullDialog = CustomDialogCommon(
                getString(R.string.common_dialog_petid_null))

            // 이미지
            (R.drawable.img_hospital_list_empty).let {
                val imgSource: Any? = when(viewModel.hospitalDetail.imageUrl[0]) {
                    "" -> AppCompatResources.getDrawable(requireContext(), it)
                    else -> viewModel.hospitalDetail.imageUrl[0]
                }

                Glide.with(requireContext())
                    .load(imgSource)
                    .placeholder(it)
                    .error(it)
                    .into(imageViewHospitalPhoto)
            }

            viewModel.hospitalDetail.run {
                textViewTitle.text = name
                textViewVet.text = vet
                textViewTime.text = hours
                textViewPlace.text = address
                textViewTel.text = tel
            }

            buttonReserve.setOnClickListener{
                when(getPreferencesControl().getIntValue(Constants.SHARED_PET_ID_VALUE)) {
                    -1 -> petidNullDialog.show(childFragmentManager, "petidNullDialog")
                    else -> infoDialog.show(childFragmentManager, "infoDialog")
                }
            }
        }
    }
}