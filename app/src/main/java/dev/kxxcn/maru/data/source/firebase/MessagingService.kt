package dev.kxxcn.maru.data.source.firebase

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.kxxcn.maru.MaruActivity
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.NotificationUtils
import dev.kxxcn.maru.util.REQUEST_CODE_NOTIFY
import org.jetbrains.anko.singleTop

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val (title, body) = message.notification
            ?.let { it.title to it.body }
            ?: message.data["title"] to message.data["body"]
        sendNotification(title, body)
    }

    private fun sendNotification(title: String?, body: String?) {
        val requestCode = REQUEST_CODE_NOTIFY
        val pendingIntent = Intent(
            this,
            MaruActivity::class.java
        ).apply {
            singleTop()
        }.run {
            PendingIntent.getActivity(
                this@MessagingService,
                requestCode,
                this,
                PendingIntent.FLAG_ONE_SHOT
            )
        }

        val channel = NotificationUtils.CHANNEL_NOTICE
        val notification = NotificationUtils.builder(this, channel)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .build()

        (getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.let { manager ->
            if (NotificationUtils.isEnable(manager, channel)) {
                manager.notify(requestCode, notification)
            }
        }
    }
}
