package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.setting.SettingFragment
import dev.kxxcn.maru.view.setting.SettingViewModel

@Module
abstract class SettingModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class,
            AuthenticationModule::class
        ]
    )
    internal abstract fun settingFragment(): SettingFragment

    @Binds
    @IntoMap
    @ViewModelKey(SettingViewModel::class)
    abstract fun bindViewModel(viewModel: SettingViewModel): ViewModel
}
