package dev.kxxcn.maru.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.kxxcn.maru.view.notification.NotificationFragment

@Module
abstract class NotificationModule {

    @ContributesAndroidInjector
    internal abstract fun notificationFragment(): NotificationFragment
}
