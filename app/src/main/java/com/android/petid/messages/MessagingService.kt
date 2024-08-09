package com.android.petid.messages
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.os.Build
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import android.widget.Toast
//import com.android.petid.R
//import com.android.petid.common.Constants.SHARED_VALUE_FCM_TOKEN
//import com.android.petid.common.PreferencesControl
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//
//class MessagingService : FirebaseMessagingService() {
//
//    companion object {
//        private const val TAG = "MessagingService"
//    }
//
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//        Log.d(TAG, "onNewToken() called with: token = $token")
//    }
//
//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//        val title = message.notification!!.title.toString()
//        val body = message.notification!!.body.toString()
//        val data = message.data
//        Log.d(TAG, "onMessageReceived: title = $title, body = $body, data = $data")
//
//        FcmUtil.showNotification(
//            context = applicationContext,
//            title = title,
//            body = body
//        )
//    }
//
//    private fun sendNotification(remoteMessage: RemoteMessage) {
//        val title = remoteMessage.data["title"]
//        val body = remoteMessage.data["body"]
//
//        Log.d(TAG,
//            "[FCM] remoteMessage.title: $title / body: $body"
//        )
//        Log.d(TAG,
//            "[FCM] remoteMessage.getData(): " + remoteMessage.data
//        )
//
//        val channelId = getString(R.string.notification_channel_id)
//        val channelName = getString(R.string.notification_channel_name)
//
//        val notificationManager =
//            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel =
//                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
//
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        // App. 실행중이면 노티 알림 표시 안함.
//        val isRunning: Boolean = Info.isRunningProcess(this, packageName)
//        Log.d(TAG,
//            "FCM isRunning: $isRunning"
//        )
//
//        if (isRunning) {
//            Log.d(
//                TAG,
//                "FCM activity call.."
//            )
//            val handler = Handler(Looper.getMainLooper())
//            handler.post {
//                Toast.makeText(
//                    applicationContext,
//                    """
//                    $title
//                    $body
//                    """.trimIndent(),
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        } else {
//            Log.d(
//                TAG,
//                "FCM Notification Show..."
//            )
//        }
//    }
//
//
//    private fun sendRegistrationToServer(token: String) {
//        Log.e(
//            TAG,
//            "[FCM] new Token -> sendRegistrationToServer token: $token"
//        )
//
//        PreferencesControl.saveStringValue(
//            applicationContext, SHARED_VALUE_FCM_TOKEN, token
//        )
//    }
//}