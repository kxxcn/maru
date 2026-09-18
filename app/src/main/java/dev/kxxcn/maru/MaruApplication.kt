package dev.kxxcn.maru

import dev.kxxcn.maru.util.preference.PreferenceUtils
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dev.kxxcn.maru.di.DaggerApplicationComponent
import dev.kxxcn.maru.util.NotificationUtils
import dev.kxxcn.maru.util.preference.PreferenceManager

open class MaruApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        PreferenceManager.init(this)
        AppCompatDelegate.setDefaultNightMode(
            if (PreferenceUtils.useDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        NotificationUtils.init(this)
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.topic_notice))
        MobileAds.initialize(this)
        PrettyFormatStrategy.newBuilder()
            .tag("Debug")
            .build()
            .let {
                Logger.addLogAdapter(object : AndroidLogAdapter(it) {
                    override fun isLoggable(priority: Int, tag: String?): Boolean {
                        return BuildConfig.DEBUG
                    }
                })
            }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this).trimMemory(level)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(this)
    }
}
