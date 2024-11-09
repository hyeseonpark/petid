package com.android.petid.ui.view.generate

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.android.petid.R
import com.android.petid.databinding.ActivityGeneratePetidMainBinding
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

        if(currentFocus is EditText) {
            currentFocus!!.clearFocus()
        }

        return super.dispatchTouchEvent(ev)
    }
}