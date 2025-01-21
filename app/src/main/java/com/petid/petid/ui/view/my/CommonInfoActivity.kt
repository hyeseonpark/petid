package com.petid.petid.ui.view.my

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.petid.petid.R
import com.petid.petid.common.Constants.COMMON_CATEGORY_TYPE
import com.petid.petid.databinding.ActivityCommonInfoBinding
import com.petid.petid.type.ContentCategoryType
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.viewmodel.my.CommonInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonInfoActivity : BaseActivity() {
    private lateinit var binding: ActivityCommonInfoBinding
    private val viewModel: CommonInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommonInfoBinding.inflate(layoutInflater).also{
            setContentView(it.root)
        }
        viewModel.categoryType = intent.getStringExtra(COMMON_CATEGORY_TYPE)?.let {
            ContentCategoryType.valueOf(it)
        }!!
    }
}