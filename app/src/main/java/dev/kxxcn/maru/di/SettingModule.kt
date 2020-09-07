package dev.kxxcn.maru.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.kxxcn.maru.view.setting.SettingFragment

@Module
abstract class SettingModule {

    @ContributesAndroidInjector
    internal abstract fun settingFragment(): SettingFragment
}
