package com.android.petid.ui.view.my

import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.petid.databinding.ActivityMyInfoBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.viewmodel.my.MyInfoViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 마이페이지 메인 > 내 정보
 */
@AndroidEntryPoint
class MyInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyInfoBinding
    private val viewModel: MyInfoViewModel by viewModels()

    private val TAG = "MyInfoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)

        initComponent()
        observeGetMemberInfoState()
        viewModel.getMemberInfo()

        setContentView(binding.root)
    }

    private fun initComponent() {
        binding.topBar.buttonBack.setOnClickListener { finish() }
        binding.topBar.textViewButton.setOnClickListener{
            val intent = Intent(this, MyInfoUpdateActivity::class.java)
            startActivity(intent)
        }
        requestCameraPermission()

        // Glide.with(this).load(R.drawable.myimage).circleCrop().into(binding.imageViewProfile);
        binding.imageViewProfile.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            filterActivityLauncher.launch(intent)
        }
    }

    /**
     * 갤러리 이미지 결과값 처리
     */
    private val filterActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK && it.data !=null) {
                var currentImageUri = it.data?.data
                try {
                    currentImageUri?.let {
                        if(Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                currentImageUri
                            )
                            binding.imageViewProfile.setImageBitmap(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, currentImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageViewProfile.setImageBitmap(bitmap)
                        }
                    }


                }catch(e:Exception) {
                    e.printStackTrace()
                }
            } else if(it.resultCode == RESULT_CANCELED){
                // Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            } else {
                Log.d("ActivityResult","something wrong")
            }
        }

    /**
     * camera permission
     */
    private fun requestCameraPermission() {
        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(this, "무야호 $it", Toast.LENGTH_SHORT).show()
        }
        permissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    /**
     * viewModel.getMemberInfoResult 결과값 view 반영
     */
    private fun observeGetMemberInfoState() {
        lifecycleScope.launch {
            viewModel.getMemberInfoResult.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        with(result.data) {

                            image?.takeIf { it.isNotBlank() }?.let {
                                Glide.with(applicationContext).load(it).into(binding.imageViewProfile)
                            }

                            binding.textViewName.text = name
                            // binding.textViewBirth.text =
                            binding.textViewPhoneNumber.text = phone
                            binding.textViewAddress.text =
                                listOfNotNull(address, addressDetails)
                                .filter { it.isNotBlank() }
                                .joinToString("\n")
                        }
                    }
                    is CommonApiState.Error -> {
                        Log.e(TAG, "${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        Log.d(TAG, "Loading....................")
                    }
                }
            }
        }
    }
}