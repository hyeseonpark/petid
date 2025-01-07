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
import com.petid.data.repository.local.NotificationRepository
import com.petid.data.source.local.entity.NotificationEntity
import com.petid.petid.R
import com.petid.petid.GlobalApplication.Companion.getGlobalContext
import com.petid.petid.di.AppFirebaseMessageServiceEntryPoint
import com.petid.petid.ui.view.main.MainActivity
import com.petid.petid.util.TAG
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val title = extras?.getString("gcm.notification.title").orEmpty()
        val body = extras?.getString("gcm.notification.body").orEmpty()

        //TODO 데이터 형식 확인 후 변환
        return if (title.isNotBlank() && body.isNotBlank()) {
            NotificationEntity(
                title = title,
                body = body,
                category = "reminder",
                status = null,
            )
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
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        val notification = NotificationCompat.Builder(getGlobalContext(), channelId)
            .setSmallIcon(R.drawable.ic_app_logo_main)
            .setColor(getColor(R.color.petid_clear_blue))
            .setContentTitle(messageData.title)
            .setContentText(messageData.body)
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
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

            // TODO 알람 정의 후 수정
            putExtra("notification_data", messageData)
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
}