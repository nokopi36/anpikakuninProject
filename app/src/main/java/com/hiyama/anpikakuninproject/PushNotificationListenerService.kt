package com.hiyama.anpikakuninproject

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hiyama.anpikakuninproject.activity.LoginActivity
import java.util.*

class PushNotificationListenerService: FirebaseMessagingService() {
    // 新しいトークンが生成された時の処理
    // 中でサーバにトークンを送信する処理などを定義
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        // チャンネルidを設定
        addChannelId()
    }

    // 通知を受信したときの処理
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // 今回は通知からタイトルと本文を取得
        val title: String = message.notification?.title.toString()
        val text: String = message.notification?.body.toString()

        // 通知表示
        sendNotification(title, text)
    }

    // 通知表示 および 見た目の設定
    private fun sendNotification(title: String, text: String) {
        // 通知タップ時に遷移するアクティビティを指定
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // 何度も遷移しないようにする（1度だけ！）
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_ONE_SHOT)

        // 通知メッセージのスタイルを設定（改行表示に対応）
        val inboxStyle = NotificationCompat.InboxStyle()
        val messageArray: List<String> = text.split("\n")
        for (msg: String in messageArray) {
            inboxStyle.addLine(msg)
        }

        // 通知音の設定
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 通知の見た目を設定
        val notificationBuilder
                = NotificationCompat.Builder(this, resources.getString(R.string.channel_id))
            .setContentTitle(title)
            .setContentText(text)
            // ステータスバーに表示されるアイコン
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            // 上で設定したpendingIntentを設定
            .setContentIntent(pendingIntent)
            // 優先度を最大化
            .setPriority(PRIORITY_MAX)
            // 通知音を出すように設定
            .setSound(defaultSoundUri)

        // 通知を実施
        // UUIDを付与することで各通知をユニーク化
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val uuid = UUID.randomUUID().hashCode()
        notificationManager.notify(uuid, notificationBuilder.build())

        // Android 8.0 以上はチャンネル設定 必須
        // strings.xml に channel_id を指定してください
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(resources.getString(R.string.channel_id))
        }
    }

    // チャンネルの設定
    private fun addChannelId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // ヘッドアップ通知を出す場合はチャンネルの重要度を最大にする必要がある
            val channel = NotificationChannel(
                resources.getString(R.string.channel_id),
                resources.getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            // ロック画面における表示レベル
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            // チャンネル登録
            manager.createNotificationChannel(channel)
        }
    }
}