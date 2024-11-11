package com.android.petid.ui.view.blog

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.domain.entity.HospitalEntity
import com.android.petid.R
import com.android.petid.databinding.ActivityContentDetailBinding
import com.android.petid.databinding.ActivityHospitalDetailBinding
import com.android.petid.enum.ContentCategoryType
import com.android.petid.ui.state.CommonApiState
import com.android.petid.ui.view.hospital.HospitalDetailActivity
import com.android.petid.ui.view.blog.adapter.HospitalListAdapter
import com.android.petid.viewmodel.blog.BlogMainViewModel
import com.android.petid.viewmodel.blog.ContentDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class ContentDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContentDetailBinding
    private val viewModel: ContentDetailViewModel by viewModels()

    private val TAG = "ContentDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentDetailBinding.inflate(layoutInflater)

        observeDoLikeState()
        observeGetContentDetailState()

        initComponent()

        setContentView(binding.root)
    }

    private fun initComponent() {
        viewModel.contentId = intent.getIntExtra("contentId", -1)
        viewModel.getContentDetail()

        // 콘텐츠 좋아요
        binding.buttonContentLike.setOnClickListener {
            viewModel.doContentLike()
        }

        binding.recyclerviewHospitalRecommendList.layoutManager = LinearLayoutManager(this)

        // TODO temp
        val hospitalList: List<HospitalEntity> = listOf(
            HospitalEntity(
                id = 581,
                imageUrl = listOf(
                    "hospital/에코동물병원.jpeg",
                    "hospital/에코동물병원2.jpeg",
                    "hospital/에코동물병원3.jpeg"
                ),
                address = "서울특별시 송파구 가락동 137번지 3호",
                name = "에코동물병원",
                hours = "09:30 - 20:00/ 토,일요일 09:30 - 17:00",
                tel = "02-443-2222",
                vet = "김미혜"
            ),
            HospitalEntity(
                id = 582,
                imageUrl = listOf("hospital/영국동물병원.jpeg"),
                address = "서울특별시 송파구 가락동 190번지 6호",
                name = "영국동물병원",
                hours = "09:30 - 19:00 / 토요일 09:30 - 18:00",
                tel = "02-430-7005",
                vet = "이영국"
            ))

        val hospitalListAdapter = HospitalListAdapter(hospitalList, this@ContentDetailActivity) { item ->
            val intent = Intent(this, HospitalDetailActivity::class.java)
                .putExtra("hospitalDetail", item)
            startActivity(intent)
        }
        binding.recyclerviewHospitalRecommendList.adapter = hospitalListAdapter

    }

    /**
     * 컨텐츠 상세 정보
     */
    private fun observeGetContentDetailState() {
        lifecycleScope.launch {
            viewModel.contentDetailApiState.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        val result = result.data

                        with(binding) {
                            textViewContentTitle.text = result.title
                            textViewContentBody.text =
                                Html.fromHtml(result.body, Html.FROM_HTML_MODE_LEGACY)

                            textViewDate.text =
                                formatInstantToDateTime(result.createdAt.split(".")[0].toLong())

                            textViewContentCategory.text = when(result.category) {
                                ContentCategoryType.RECOMMENDED.name -> getString(R.string.tab_recommendation_title)
                                ContentCategoryType.TIPS.name -> getString(R.string.tab_tips_title)
                                ContentCategoryType.ABOUTPET.name -> getString(R.string.tab_about_pet_title)
                                ContentCategoryType.VENUE.name -> getString(R.string.tab_venue_title)
                                ContentCategoryType.SUPPORT.name -> getString(R.string.tab_support_title)
                                else -> ""
                            }

                            textViewLike.text =
                                String.format(getString(R.string.content_like_desc), result.likesCount)
                            buttonContentLike.isSelected = result.isLiked

                            /*contentItem.imageUrl?.let {
                                Glide.with(mContext)
                                    .load(it)
                                    .into(imageViewContentPreview)
                            }*/
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

    /**
     * 콘텐츠 좋아요 하기
     */
    private fun observeDoLikeState() {
        lifecycleScope.launch {
            viewModel.doLikeApiResult.collectLatest { result ->
                when (result) {
                    is CommonApiState.Success -> {
                        val resultData = result.data
                        // TODO api result 값 수정 되면 화면 반영
                        if (!binding.buttonContentLike.isSelected) {
                            binding.buttonContentLike.isSelected = true
                        }
                        binding.textViewLike.text =
                            String.format(getString(R.string.content_like_desc), resultData.likeCount)

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

    /**
     * date 값 변환
     */
    private fun formatInstantToDateTime(timestamp: Long): String {
        val instant = Instant.ofEpochSecond(timestamp)

        val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일(E) HH:mm", Locale.KOREAN)

        return dateTime.format(formatter)
    }
}