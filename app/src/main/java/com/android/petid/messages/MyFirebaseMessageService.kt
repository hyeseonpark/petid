package com.android.petid.messages

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.petid.R
import com.android.petid.common.GlobalApplication.Companion.getGlobalContext
import com.android.petid.ui.view.main.MainActivity
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
        Log.d(TAG,"[FCM] Message get Data size: " + remoteMessage.data)

        val notificationData = remoteMessage.notification

        val notificationType = remoteMessage.data["notiType"].orEmpty()
        val title = notificationData?.title
        val body = notificationData?.body

        if (title != null && body != null && notificationType != null) {
            Log.d(TAG,"[FCM] Message get Data: " + notificationData)
            sendNotificationTemp(notificationData.title.toString(), notificationData.body.toString())
        }

        // Check if message contains a data payload.
        /*if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG,"[FCM] Message get Data: " + remoteMessage.data)
            sendNotification(remoteMessage)
        }*/
    }

    private fun sendNotificationTemp(title: String, body: String) {
        val channelId = getString(R.string.notification_channel_id)
        val channelName = getString(R.string.notification_channel_name)
        val notificationId = Math.random().toInt()

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

            notificationManager.createNotificationChannel(channel)
        }

        val manager = NotificationManagerCompat.from(getGlobalContext())
        val channel = NotificationChannelCompat
            .Builder(channelId, NotificationManagerCompat.IMPORTANCE_HIGH)
            .setName(channelName)
            .build()

        manager.createNotificationChannel(channel)

        /*val intent = Intent(getGlobalContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION

        val pendingIntent = PendingIntent
            .getActivity(getGlobalContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(getGlobalContext(), channelId)
            .setSmallIcon(R.drawable.ic_app_logo_main)
            .setTicker("Downloading...")
            .setContentTitle("Downloading...")
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setAutoCancel(false)
            .setFullScreenIntent(pendingIntent, true)*/


        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_app_logo_main)

        val intent = Intent(getGlobalContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION

        val pendingIntent = PendingIntent
            .getActivity(getGlobalContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(getGlobalContext(), channel.id)
            .setSmallIcon(R.drawable.ic_app_logo_main)
            .setContentTitle(title)
            .setContentText(body)
            .setLargeIcon(bitmap)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap))
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setFullScreenIntent(pendingIntent, true)

        manager.notify(notificationId, builder.build())
        /*if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }*/
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