package com.petid.petid.ui.view.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.petid.data.source.local.entity.NotificationEntity
import com.petid.petid.R
import com.petid.petid.common.Constants.NOTIFICATION_DATA
import com.petid.petid.databinding.ActivityMainBinding
import com.petid.petid.ui.component.CustomDialogCommon
import com.petid.petid.ui.view.common.BaseActivity
import com.petid.petid.util.hideKeyboardAndClearFocus
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navMainFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_layout_main) as NavHostFragment
        navController = navMainFragment.findNavController()

        val notificationData = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(NOTIFICATION_DATA, NotificationEntity::class.java)
        } else {
            intent.getParcelableExtra(NOTIFICATION_DATA) as? NotificationEntity
        }
        if (notificationData != null) handleNotificationIntent(notificationData)

        binding.bottomNavigation.let {
            it.setupWithNavController(navController)
            disableBottomNavigationViewTooltips(it)
        }
    }

    /**
     * notification 분기처리
     */
    // TODO 알람 정의 후 수정
    private fun handleNotificationIntent(data: NotificationEntity) {
        when (data.category) {
            "reminder" -> showReminderDialog(data.category, data.desc)
                .show(this.supportFragmentManager, null)
            "booking" -> {}
            "order" -> {}
            /*"NAVIGATE" -> {
                // 새로운 그래프로 이동
                navController.navigate(R.id.action_global_to_other_graph)
                // 그 다음 특정 화면으로 이동
                navController.navigate(R.id.specific_destination, bundleOf(
                    "notification" to notification
                ))
            }*/
            // 다른 타입들 처리
        }
    }

    /**
     * Reminder dialog
     */
    private fun showReminderDialog(title: String, body: String) = CustomDialogCommon(
        boldTitle = title,
        title = body,
        isSingleButton = true,
        singleButtonText = getString(R.string.hospital_make_reservation_dialog_info_button))

    /**
     * EditText 바깥 터치 시 키보드 숨기기
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboardAndClearFocus()
        return super.dispatchTouchEvent(ev)
    }

    /**
     * tooltip 제거
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("RestrictedApi")
    private fun disableBottomNavigationViewTooltips(bottomNavigationView: BottomNavigationView) {
        val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        for (i in 0 until menuView.childCount) {
            val itemView = menuView.getChildAt(i)
            itemView.setOnLongClickListener { true }
            itemView.tooltipText = null
        }
    }
}