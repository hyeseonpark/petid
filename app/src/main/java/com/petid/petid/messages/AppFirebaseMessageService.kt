package com.petid.petid.messages

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.Constants.MessageNotificationKeys
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.petid.data.dto.response.NotificationBody
import com.petid.data.repository.local.NotificationRepository
import com.petid.data.source.local.entity.NotificationEntity
import com.petid.petid.R
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.common.Constants.NOTIFICATION_DATA
import com.petid.petid.di.AppFirebaseMessageServiceEntryPoint
import com.petid.petid.ui.view.main.MainActivity
import com.petid.petid.util.TAG
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Push Service를 받아 Room DB에 동기화를 시키고 사용자에게 알림을 통지하는 서비스.
 * 알림 동기화 목적 : Notification을 DB History화 하여 사용자가 중요 알림을 다시 확인하기 위함
 *
 * Author : 혜선
 */
class AppFirebaseMessageService() : FirebaseMessagingService() {

    private val notificationRepository: NotificationRepository by lazy {
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            AppFirebaseMessageServiceEntryPoint::class.java
        )
        entryPoint.notificationRepository()
    }

    override fun handleIntent(intent: Intent?) {
        val new = intent?.apply {
            val temp = extras?.apply {
                remove(MessageNotificationKeys.ENABLE_NOTIFICATION)
                remove(keyWithOldPrefix(MessageNotificationKeys.ENABLE_NOTIFICATION))
            }
            replaceExtras(temp)
        }
        super.handleIntent(new)
    }

    private fun keyWithOldPrefix(key: String): String {
        if (!key.startsWith(MessageNotificationKeys.NOTIFICATION_PREFIX)) {
            return key
        }

        return key.replace(
            MessageNotificationKeys.NOTIFICATION_PREFIX,
            MessageNotificationKeys.NOTIFICATION_PREFIX_OLD
        )
    }

    /**
     * 새로운 토큰이 생성될 때 마다 해당 콜백 호출
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "onNewToken: $token")
        // TODO update Token
    }

    /**
     *
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            val messageData = extractMessageData(remoteMessage)
            if (messageData != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    notificationRepository.saveNotification(messageData)
                }
                sendNotification(messageData)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing FCM message", e)
        }
    }

    /**
     * remoteMessgae -> NotificationEntity로 변환
     */
    private fun extractMessageData(remoteMessage: RemoteMessage): NotificationEntity? {
        val extras = remoteMessage.toIntent().extras
        val category = extras?.getString("gcm.notification.title").orEmpty()
        val body = extras?.getString("gcm.notification.body").orEmpty()

        val jsonBody = body
            .replace("=", "\":\"")
            .replace("{", "{\"")
            .replace("}", "\"}")
            .replace(", ", "\", \"")
            .replace("hostpitalName", "hospitalName")

        val gson = Gson()
        val notificationBody = gson.fromJson(jsonBody, NotificationBody::class.java)

        return if (category.isNotBlank() && body.isNotBlank()) {
            with(notificationBody) {
                NotificationEntity(
                    notiTitle = getNotiTitle(category, status, id),
                    notiBody = getNotiBody(category, status, id, hospitalName),
                    category = category,
                    status = status,
                    hospitalName = hospitalName,
                    detailId = id?.toInt(),
                )
            }
        } else {
            Log.w(TAG, "Received message with empty title or body")
            null
        }
    }

    /**
     * sendNotification
     */
    private fun sendNotification(messageData: NotificationEntity) {
        if (!hasNotificationPermission()) {
            Log.w(TAG, "Notification permission not granted")
            return
        }

        val channelId = getString(R.string.notification_channel_id)
        val manager = NotificationManagerCompat.from(applicationContext)
        val notificationId = System.currentTimeMillis().toInt()

        createNotificationChannel(manager, channelId)

        val pendingIntent = PendingIntent
            .getActivity(
                getGlobalContext(),
                notificationId,
                createNotificationIntent(messageData),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val notification = NotificationCompat.Builder(getGlobalContext(), channelId)
            .setSmallIcon(R.drawable.ic_app_logo_main)
            .setColor(getColor(R.color.petid_clear_blue))
            .setContentTitle(messageData.notiTitle)
            .setContentText(messageData.notiBody)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setFullScreenIntent(pendingIntent, true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            manager.notify(notificationId, notification)
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to show notification", e)
        }
    }

    /**
     * create notification channel
     */
    private fun createNotificationChannel(
        manager: NotificationManagerCompat,
        channelId: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = getString(R.string.notification_channel_name)
            val channel = NotificationChannelCompat.Builder(
                channelId,
                NotificationManagerCompat.IMPORTANCE_HIGH
            )
                .setName(channelName)
                .setVibrationEnabled(true)
                .build()

            manager.createNotificationChannel(channel)
        }
    }

    /**
     * notification intent
     */
    private fun createNotificationIntent(messageData: NotificationEntity) =
        Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

            // TODO 알람 정의 후 수정
            putExtra(NOTIFICATION_DATA, messageData)
            //data = Uri.parse("petid://notification/$messageData")
        }

    /**
     * has notification permission
     */
    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * get notification title
     */
    private fun getNotiTitle(category: String, status: String, detailId: String?): String {
        return when(category) {
            "booking" -> {
                when(status) {
                    "CONFIRMED" -> "동물병원 예약 확정"
                    "CANCELLED" -> "예약 취소 알림"
                    else -> ""
                }
            }
            "order" -> {
                when(status) {
                    "COMPLETED" -> "예약 방문 완료"
                    "PENDING" -> "예약 방문 알림"
                    else -> ""
                }
            }
            "reminder" -> {
                when(detailId == null) {
                    true -> "동물등록 대상"
                    false -> "펫 블로그 새로운 글"
                }
            }
            else -> ""
        }
    }

    /**
     * get notification body
     */
    private fun getNotiBody(category: String, status: String, detailId: String?, hospitalName: String): String {
        return when(category) {
            "booking" -> {
                when(status) {
                    "CONFIRMED" -> "'${hospitalName}' 예약이 확정되었습니다. 예약 정보를 확인하시고 방문해주세요."
                    "CANCELLED" -> "'${hospitalName}' 사정으로 예약이 취소되었습니다. 앱에서 다시 예약 도와드릴게요!"
                    else -> ""
                }
            }
            "order" -> {
                when(status) {
                    "COMPLETED" -> "동물 병원을 다녀오신 회원님의 상태를 변경해 주세요!"
                    "PENDING" -> "1시간 후 '${hospitalName}' 예약 방문 일정이 있습니다. 방문 전 병원 정보를 다시 한 번 확인해 주세요."
                    else -> ""
                }
            }
            "reminder" -> {
                when(detailId == null) {
                    true -> "강아지 눈에 있는 길다린 실, 기생충 일 수 있다!?"
                    false -> "간단한 반려동물등록으로 평생을 함께하는 방법 지금 알려드려요!"
                }
            }
            else -> ""
        }
    }
}