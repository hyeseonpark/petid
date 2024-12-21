package com.android.petid.messages

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.android.petid.R
import com.android.petid.util.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessageService : FirebaseMessagingService() {

    /**
     * 새로운 토큰이 생성될 때 마다 해당 콜백 호출
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "onNewToken: $token")
        sendRegistrationToServer(token)
    }

    /**
     *
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG,"[FCM] From: " + remoteMessage.from)
        Log.d(TAG,"[FCM] Message get Data size: " + remoteMessage.data.size)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG,"[FCM] Message get Data: " + remoteMessage.data)
            sendNotification(remoteMessage)
        }
    }

    /**
     * Foreground에서 Push Service를 받기 위해 Notification 설정
     */
    private fun sendNotification(remoteMessage: RemoteMessage) {

        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]

        Log.d(TAG, "[FCM] remoteMessage.title: $title / body: $body")
        Log.d(TAG, "[FCM] remoteMessage.getData(): " + remoteMessage.data)

        val channelId = getString(R.string.notification_channel_id)
        val channelName = getString(R.string.notification_channel_name)

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

            notificationManager.createNotificationChannel(channel)
        }


        // App. 실행중이면 노티 알림 표시 안함.
        /* val isRunning: Boolean = Info.isRunningProcess(this, packageName)
        Log.d(TAG,"FCM isRunning: $isRunning")

        if (isRunning) {
            Log.d(TAG,"FCM activity call..")
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Toast.makeText(
                    applicationContext,
                    """
                $title
                $body
                """.trimIndent(),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Log.d(TAG,"FCM Notification Show...")
        }*/
    }

    /**
     *
     */
    private fun sendRegistrationToServer(token: String) {
//        Log.e(TAG,"[FCM] new Token -> sendRegistrationToServer token: $token")

        /*PreferencesControl.saveStringValue(
            applicationContext, SHARED_VALUE_FCM_TOKEN, token
        )*/
    }
}