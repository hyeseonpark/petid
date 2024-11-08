package com.android.petid.ui.view.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.petid.R
import com.android.petid.databinding.FragmentPetPhotoBinding

class PetPhotoFragment : Fragment() {
    private lateinit var binding: FragmentPetPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPetPhotoBinding.inflate(layoutInflater)
        initComponent()

        return binding.root
    }

    fun initComponent() {

//        binding.textViewStep2.text =
//            setStyleSpan(applicationContext, binding.textViewStep2.text.toString(),
//                resources.getString(R.string.pet_photo_activity_step_2_span), R.color.petid_clear_blue, true)

        binding.buttonTakingPhoto.button.setOnClickListener{
             findNavController().navigate(R.id.action_petPhotoFragment_to_scannedInfoFragment)
        }
    }
}
