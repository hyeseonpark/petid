package com.android.petid.ui.view.generate

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.android.petid.R
import com.android.petid.databinding.ActivityGeneratePetidMainBinding
import com.android.petid.util.hideKeyboardAndClearFocus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeneratePetidMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGeneratePetidMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGeneratePetidMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        supportFragmentManager.findFragmentById(R.id.fragment_layout_generate) as NavHostFragment
    }

    /**
     * EditText 바깥 터치 시 키보드 숨기기
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboardAndClearFocus()
        return super.dispatchTouchEvent(ev)
    }
}