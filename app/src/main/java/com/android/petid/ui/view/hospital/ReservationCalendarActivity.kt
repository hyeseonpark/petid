package com.android.petid.ui.view.hospital

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.geometry.CornerRadius
import androidx.lifecycle.lifecycleScope
import com.android.petid.R
import com.android.petid.common.Constants.DAYS_OF_WEEK
import com.android.petid.databinding.ActivityReservationCalendarBinding
import com.android.petid.ui.state.CommonApiState
import com.android.petid.viewmodel.hospital.ReservationCalendarViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class ReservationCalendarActivity : AppCompatActivity() {
    lateinit var binding: ActivityReservationCalendarBinding
    private val viewModel: ReservationCalendarViewModel by viewModels()

    private val TAG = "ReservationCalendarActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponent()

        observeHospitalOrderTimeList()
        observeCreateHospitalOrder()
    }

    private fun initComponent() {
        viewModel.hospitalId = intent.getIntExtra("hospitalId", -1)
        binding.topBar.title = intent.getStringExtra("hospitalName")

        // 선택된 날짜 변경 시
        binding.calendarView.setOnDateChangedListener{ widget, date, selected ->
            triggerDateChangeListener(date)
        }

        // 선택 가능한 날짜 범위: from
        val tomorrow = CalendarDay.from(LocalDate.now().plusDays(1))
        binding.calendarView.currentDate = tomorrow
        binding.calendarView.selectedDate = tomorrow

        triggerDateChangeListener(tomorrow)

        // 선택 가능한 날짜 범위: to
        val maxCalendar = Calendar.getInstance()
        maxCalendar.add(Calendar.DAY_OF_YEAR, 7)

        binding.calendarView.state().edit()
            .setMinimumDate(
                CalendarDay.from(
                    tomorrow.year,
                    tomorrow.month,
                    tomorrow.day
                )
            ).setMaximumDate(
                CalendarDay.from(
                    maxCalendar.get(Calendar.YEAR),
                    maxCalendar.get(Calendar.MONTH) + 1,
                    maxCalendar.get(Calendar.DAY_OF_MONTH)
                )
            )
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()

        // 예약 완료 버튼
        binding.buttonConfirm.button.setOnClickListener{
            viewModel.createHospitalOrder()
        }
    }

    /**
     * 선택된 date에 따른 예약가능한 시간 목록 업데이트
     */
    private fun triggerDateChangeListener(date: CalendarDay) {
        val dateFormat = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())

        // day 계산
        val calendar = Calendar.getInstance().apply {
            set(date.year, date.month - 1, date.day)
        }

        // request params 저장 및 예약 가능 시간 목록 조회 api
        viewModel.day = DAYS_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1]
        viewModel.dateStr = simpleDateFormat.format(calendar.time)
        viewModel.getHospitalOrderTimeList()

        // 선택된 날짜 값 저장
        viewModel.selectedDateTime = calendar.time
    }

    /**
     * 예약가능시간 조회
     */
    private fun observeHospitalOrderTimeList() {
        lifecycleScope.launch {
            viewModel.hospitalOrderTimeApiState.collectLatest { result ->
                when(result) {
                    is CommonApiState.Success -> {
                        val orderTimeList = result.data

                        if (orderTimeList.isNotEmpty()) {
                            // 예약 가능 시간이 있을 때
                            binding.availableTimeLayout.visibility = View.VISIBLE
                            binding.dayOffLayout.visibility = View.GONE
                            initChipGroup(orderTimeList)
                        } else {
                            // 예약 가능 시간이 없을 때
                            binding.availableTimeLayout.visibility = View.GONE
                            binding.dayOffLayout.visibility = View.VISIBLE
                            binding.buttonConfirm.disable = true
                        }
                    }
                    is CommonApiState.Error -> {
                        // 오류 처리
                        Log.e(TAG, "Login error: ${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        // 로딩 상태 처리
                        Log.d(TAG, "Loading....................")
                    }
                }
            }
        }
    }

    /**
     * 예약 요청 api
     */
    private fun observeCreateHospitalOrder() {
        lifecycleScope.launch {
            viewModel.createHospitalOrderApiState.collect { result ->
                when(result) {
                    is CommonApiState.Success -> {
                        val intent = Intent(
                            this@ReservationCalendarActivity,
                            ReservationProcessFinishActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is CommonApiState.Error -> {
                        // 오류 처리
                        Log.e(TAG, "Login error: ${result.message}")
                    }
                    is CommonApiState.Loading -> {
                        // 로딩 상태 처리
                        Log.d(TAG, "Loading....................")
                    }
                }
            }
        }
    }

    /**
     * 예약가능시간 목록
     */
    private fun initChipGroup(orderTimeList: List<String>) {
        // chip group 목록 초기화
        binding.morningChipGroup.removeAllViews()
        binding.afternoonChipGroup.removeAllViews()

        // orderTimeList 데이터에 따른 Chip 추가
        orderTimeList.forEach { time ->
            // init chip style
            var chip = Chip(this).apply {
                isCheckable = true
                chipStrokeWidth = 1f
                CornerRadius(6f, 6f)

                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
                setPadding(0, 0, 0, 0)
                setTypeface(null, Typeface.NORMAL)
                setTextAlignment(View.TEXT_ALIGNMENT_CENTER)

                // 테두리
                chipStrokeColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)
                    ), intArrayOf(Color.parseColor("#E9E9E9"), Color.TRANSPARENT))

                // 백그라운드
                chipBackgroundColor = ColorStateList(
                    arrayOf(
                        intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)
                    ), intArrayOf(Color.WHITE, resources.getColor(R.color.petid_clear_blue, null))
                )

                // 텍스트
                setTextColor(
                    ColorStateList(
                        arrayOf(
                            intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                        intArrayOf(resources.getColor(R.color.petid_title, null), Color.WHITE)
                    )
                )


                // 크기 및 여백 설정
                val layoutParams = ChipGroup.LayoutParams(
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics).toInt(),
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, resources.displayMetrics).toInt()
                ).apply {
                    setMargins(
                        0, 0,
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13f, resources.displayMetrics).toInt(),
                        0
                    )
                    gravity = Gravity.CENTER
                }
                this.layoutParams = layoutParams
            }

            chip.text = time

            // 오전/오후 구분
            if (time < "12:00") {
                binding.morningChipGroup.addView(chip)
            } else {
                binding.afternoonChipGroup.addView(chip)
            }

            // Chip 선택 시 텍스트 값 업데이트
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.selectedDateTime?.let { selectedDateTime ->
                        val timeParts = chip.text.toString().split(":")

                        if (timeParts.size == 2) {
                            // 시간과 분을 각각 Int로 변환하여 저장
                            val selectedHour = timeParts[0].toIntOrNull() // 시간
                            val selectedMinute = timeParts[1].toIntOrNull() // 분

                            selectedHour?.let { hour ->
                                selectedMinute?.let { minute ->
                                    // 기존에 있는 날짜 정보는 유지하고 시간만 설정
                                    val calendar = Calendar.getInstance().apply {
                                        this.time = selectedDateTime // 기존 Date 객체에서 시간 정보 가져오기
                                        set(Calendar.HOUR_OF_DAY, hour) // 시간 설정
                                        set(Calendar.MINUTE, minute) // 분 설정
                                        set(Calendar.SECOND, 0) // 초는 0으로 설정 (필요시 수정 가능)
                                    }

                                    // 변경된 시간 정보를 다시 selectedDateTime에 저장
                                    viewModel.selectedDateTime = calendar.time
                                }
                            }
                        }
                    }
                    binding.buttonConfirm.disable = false
                } else {
                    // 선택된 chip이 없는 경우
                    binding.buttonConfirm.disable = true
                }
            }
        }

        // 오전 ChipGroup과 오후 ChipGroup의 선택 상태를 동기화
        binding.morningChipGroup.setOnCheckedStateChangeListener() { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                binding.afternoonChipGroup.clearCheck() // 오후 그룹 선택 해제
            }
        }

        binding.afternoonChipGroup.setOnCheckedStateChangeListener() { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                binding.morningChipGroup.clearCheck() // 오전 그룹 선택 해제
            }
        }
    }

}