package com.android.petid.view.generate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.petid.databinding.ActivityPetPhotoBinding

class PetPhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetPhotoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.textViewStep2.text =
//            setStyleSpan(applicationContext, binding.textViewStep2.text.toString(),
//                resources.getString(R.string.pet_photo_activity_step_2_span), R.color.petid_clear_blue, true)

        binding.buttonTakingPhoto.button.setOnClickListener{
            val intent = Intent(this, ScannedInfoActivity::class.java)
            startActivity(intent)
        }
    }
}
