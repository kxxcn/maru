package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.splash.SplashFragment
import dev.kxxcn.maru.view.splash.SplashViewModel

@Module
abstract class SplashModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun splashFragment(): SplashFragment

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindViewModel(viewModel: SplashViewModel): ViewModel
}
