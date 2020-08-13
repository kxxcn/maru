package dev.kxxcn.maru.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import dev.kxxcn.maru.BuildConfig
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.preference.PreferenceUtils
import org.jetbrains.anko.notificationManager

object NotificationUtils {

    const val CHANNEL_NOTICE = "dev.kxxcn.maru.util.NotificationUtils.CHANNEL_NOTICE"

    private fun defaultBeep() =
        "android.resource://${BuildConfig.APPLICATION_ID}/${R.raw.beep_default}"

    fun makeChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.notificationManager
            listOf(CHANNEL_NOTICE).also { ids ->
                val channels = ids.map { config(context, it).toChannel() }
                channels.forEach { channel -> manager.createNotificationChannel(channel) }
            }
        }
    }

    fun builder(context: Context, channel: String): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, channel)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            val config = config(context, channel)
            config.vibrate?.let { builder.setVibrate(it) }
            config.sound?.let { builder.setSound(it) }
        }
        return builder
    }

    fun isEnable(manager: NotificationManager, channel: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nc = manager.getNotificationChannel(channel)
            if (nc == null) {
                false
            } else {
                nc.importance > NotificationManager.IMPORTANCE_NONE
            }
        } else {
            PreferenceUtils.notifyNotice
        }
    }

    private fun config(context: Context, channel: String): Config {
        return Config(
            channel,
            context.getString(R.string.more_notice),
            if (PreferenceUtils.notifyNoticeVibrate) longArrayOf(0, 800) else longArrayOf(0),
            if (PreferenceUtils.notifyNoticeSound) Uri.parse(defaultBeep()) else null
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Config.toChannel(): NotificationChannel {
        val config = this
        return NotificationChannel(
            config.id,
            config.name,
            config.importance.managedValue()
        ).apply {
            enableLights(true)
            val vibrate = config.vibrate
            if (vibrate != null) {
                enableVibration(true)
                vibrationPattern = vibrate
            } else {
                enableVibration(false)
            }
            setSound(config.sound, Notification.AUDIO_ATTRIBUTES_DEFAULT)
        }
    }

    class Config(
        val id: String,
        val name: String,
        val vibrate: LongArray?,
        val sound: Uri?,
        val importance: Importance = Importance.HIGH
    )

}

enum class Importance {

    HIGH, DEFAULT, LOW;

    fun managedValue(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when (this) {
                HIGH -> NotificationManager.IMPORTANCE_HIGH
                DEFAULT -> NotificationManager.IMPORTANCE_DEFAULT
                LOW -> NotificationManager.IMPORTANCE_LOW
            }
        } else {
            @Suppress("DEPRECATION")
            when (this) {
                HIGH -> Notification.PRIORITY_MAX
                DEFAULT -> Notification.PRIORITY_DEFAULT
                LOW -> Notification.PRIORITY_LOW
            }
        }
    }
}

