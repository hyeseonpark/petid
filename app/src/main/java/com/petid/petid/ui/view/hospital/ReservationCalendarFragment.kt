package com.petid.petid.ui.view.hospital

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.geometry.CornerRadius
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.petid.petid.R
import com.petid.petid.ui.view.common.BaseFragment
import com.petid.petid.common.Constants.DAYS_OF_WEEK
import com.petid.petid.databinding.FragmentReservationCalendarBinding
import com.petid.petid.ui.state.CommonApiState
import com.petid.petid.util.showErrorMessage
import com.petid.petid.viewmodel.hospital.HospitalViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.petid.petid.util.throttleFirst
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.TitleFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import ru.ldralighieri.corbind.material.checkedChanges
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.checkedChanges
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class ReservationCalendarFragment:
    BaseFragment<FragmentReservationCalendarBinding>(FragmentReservationCalendarBinding::inflate) {

    private val viewModel: HospitalViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationCalendarBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(
            toolbar = view.findViewById(R.id.toolbar),
            showBackButton = true,
            title = viewModel.hospitalDetail.name
        )
        initComponent()

        observeHospitalOrderTimeList()
        observeCreateHospitalOrder()
    }

    /**
     * init component
     */
    private fun initComponent() {
        with(binding) {
            viewModel.hospitalId = viewModel.hospitalId

            // 예약 완료 버튼
            buttonConfirm
                .clicks()
                .throttleFirst()
                .onEach {
                    viewModel.createHospitalOrder()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            // chip group 줄 간격 설정
            morningChipGroup.chipSpacingVertical = 0
            afternoonChipGroup.chipSpacingVertical = 0

            initCalendarView()
        }
    }

    /**
     * Calendar View init
     */
    private fun initCalendarView() {
        with(binding.calendarView) {

            // 선택 가능한 날짜 범위: from
            val tomorrow = CalendarDay.from(LocalDate.now().plusDays(1))

            val todayDecorator = TodayDecorator(context)
            addDecorators(todayDecorator)

            // 좌우 화살표 사이 연, 월의 폰트 스타일 설정
            setHeaderTextAppearance(R.style.CalendarWidgetHeader)
            // 타이틀 형식 변경
            setTitleFormatter(CustomTitleFormatter())

            // 선택된 날짜 변경 시
            setOnDateChangedListener { widget, date, selected ->
                triggerDateChangeListener(date)

                // decorator 초기화
                removeDecorators()
                invalidateDecorators()

                //
                val dayDecorator = DayDecorator(context, date)
                addDecorators(todayDecorator, dayDecorator)
            }

            // 선택 가능한 날짜 범위: to
            val maxCalendar = Calendar.getInstance()
            maxCalendar.add(Calendar.DAY_OF_YEAR, 7)

            state().edit()
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
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when(result) {
                    is CommonApiState.Success -> {
                        val orderTimeList = result.data

                        with(binding) {
                            if (orderTimeList.isNotEmpty()) {
                                // 예약 가능 시간이 있을 때
                                availableTimeLayout.visibility = View.VISIBLE
                                defaultLayout.visibility = View.GONE
                                initChipGroup(orderTimeList)
                            } else {
                                // 예약 가능 시간이 없을 때
                                availableTimeLayout.visibility = View.GONE
                                defaultLayout.visibility = View.VISIBLE
                                textViewDefaultTitle.text = getString(R.string.hospital_reservation_calendar_day_off)
                                textViewDefaultDesc.text = getString(R.string.hospital_reservation_calendar_day_off_desc)
                                buttonConfirm.isEnabled = false
                            }
                        }
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
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
                if (result !is CommonApiState.Loading)
                    hideLoading()

                when(result) {
                    is CommonApiState.Success -> {
                        findNavController().navigate(
                            R.id.action_reservationCalendarFragment_to_reservationProcessFinishFragment)
                    }
                    is CommonApiState.Error -> showErrorMessage(result.message.toString())
                    is CommonApiState.Loading -> showLoading()
                    is CommonApiState.Init -> {}
                }
            }
        }
    }

    /**
     * 예약가능시간 목록
     */
    private fun initChipGroup(orderTimeList: List<String>) {
        with(binding) {
            // chip group 목록 초기화
            morningChipGroup.removeAllViews()
            afternoonChipGroup.removeAllViews()

            // orderTimeList 데이터에 따른 Chip 추가
            orderTimeList.forEach { time ->
                // init chip style
                var chip = Chip(context).apply {
                    isCheckable = true
                    chipStrokeWidth = 1f
                    CornerRadius(6f, 6f)

                    setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
                    setPadding(0, 0, 0, 0)
                    setTypeface(null, Typeface.NORMAL)
                    textAlignment = View.TEXT_ALIGNMENT_CENTER

                    // 테두리
                    chipStrokeColor = ColorStateList(
                        arrayOf(
                            intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)
                        ), intArrayOf(resources.getColor(R.color.e9), Color.TRANSPARENT))

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
                    morningChipGroup.addView(chip)
                } else {
                    afternoonChipGroup.addView(chip)
                }

                // Chip 선택 시 텍스트 값 업데이트
                chip
                    .checkedChanges()
                    .onEach { isChecked ->
                        if (isChecked) {
                            viewModel.selectedDateTime.let { selectedDateTime ->
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
                            buttonConfirm.isEnabled = true
                        } else {
                            // 선택된 chip이 없는 경우
                            buttonConfirm.isEnabled = false
                        }
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            }

            // 오전 ChipGroup과 오후 ChipGroup의 선택 상태를 동기화
            morningChipGroup
                .checkedChanges()
                .onEach { checkedIds ->
                    if (checkedIds.isNotEmpty()) {
                        afternoonChipGroup.clearCheck() // 오후 그룹 선택 해제
                    }
                }
                .launchIn(lifecycleScope)

            afternoonChipGroup
                .checkedChanges()
                .onEach { checkedIds ->
                    if (checkedIds.isNotEmpty()) {
                        morningChipGroup.clearCheck() // 오전 그룹 선택 해제
                    }
                }
                .launchIn(lifecycleScope)
        }
    }

    /**
     * CalendarView Custom : 년월 타이틀 형식 변경
     */
    inner class CustomTitleFormatter : TitleFormatter {
        override fun format(day: CalendarDay?): CharSequence {
            val simpleDateFormat =
                SimpleDateFormat(getString(R.string.hospital_reservation_calendar_title), Locale.KOREA)

            return simpleDateFormat.format(Calendar.getInstance().time)
        }

    }

    /**
     * CalendarView Custom : 선택한 날짜 drawable 설정
     */
    private inner class DayDecorator(context: Context, private val selectedDate: CalendarDay) : DayViewDecorator {
        private val drawable = ContextCompat.getDrawable(context,R.drawable.ic_calendar_selector)

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day.equals(selectedDate)
        }

        override fun decorate(view: DayViewFacade) {
            view.setSelectionDrawable(drawable!!)
        }
    }

    /**
     * CalendarView Custom : 오늘 날짜의 색상 설정
     */
    private class TodayDecorator(context: Context) : DayViewDecorator {
        private var date = CalendarDay.today()
        private val color = ContextCompat.getColor(context,R.color.petid_clear_blue)

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day.equals(date)
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(object: ForegroundColorSpan(color){})
        }
    }
}