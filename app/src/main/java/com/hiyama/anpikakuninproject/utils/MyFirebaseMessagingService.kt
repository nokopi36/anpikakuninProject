package com.hiyama.anpikakuninproject.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.activity.LoginActivity

class MyFirebaseMessagingService :FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.i("Refreshed token: $token", "a")

        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // tokenをserverへ送る
        Log.i("sendRegistrationTokenToServer($token)", "a")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.i("From: ${remoteMessage.from}", "a")

        // Check if message contains a notification payload.
        remoteMessage.notification?.body?.let { body ->
            Log.i("Message Notification Body:$body", "a")
//            sendNotification(body)
            remoteMessage.notification?.title?.let { title ->
                Log.i("Message Notification Title:$title", "a")
                sendNotification(body,title)
            }
        }
    }

    // notificationを生成して表示
    private fun sendNotification(messageBody: String, messageTitle: String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}