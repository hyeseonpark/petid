package com.android.petid.ui.view.hospital

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.petid.databinding.ActivityReservationCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.Calendar


class ReservationCalendarActivity : AppCompatActivity() {
    lateinit var binding: ActivityReservationCalendarBinding
    fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)
    fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1
    fun getCurrentDay(): Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservationCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponent()
    }

    private fun initComponent() {

        binding.buttonConfirm.button.setOnClickListener{
            val intent = Intent(this, CompleteReservationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent)
            finish()
        }

        val maxCalendar = Calendar.getInstance()
        maxCalendar.add(Calendar.DAY_OF_YEAR, 7)

        binding.calendarView.state().edit()
            .setMinimumDate(
                CalendarDay.from(
                    getCurrentYear(),
                    getCurrentMonth(),
                    getCurrentDay()
                )
            ).setMaximumDate(
                CalendarDay.from(
                    maxCalendar.get(Calendar.YEAR),
                    maxCalendar.get(Calendar.MONTH) + 1,
                    maxCalendar.get(Calendar.DAY_OF_MONTH)
                )
            )
            .setCalendarDisplayMode(CalendarMode.WEEKS)
            .commit()

        /*binding.calendarView.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay): Boolean {
                val cal1: Calendar = Calendar.getInstance()
                val cal2 = Calendar.getInstance()

                return (cal1[Calendar.ERA] == cal2[Calendar.ERA] && cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR])
            }

            override fun decorate(view: DayViewFacade) {
                view.setBackgroundDrawable(
                    ContextCompat.getDrawable(this@ReservationCalendarActivity,
                        com.android.petid.R.drawable.ic_search
                    )!!
                )
            }
        })*/
    }

}