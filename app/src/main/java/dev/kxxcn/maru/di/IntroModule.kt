package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.intro.IntroFragment
import dev.kxxcn.maru.view.intro.IntroViewModel

@Module
abstract class IntroModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class,
            AuthenticationModule::class
        ]
    )
    internal abstract fun introFragment(): IntroFragment

    @Binds
    @IntoMap
    @ViewModelKey(IntroViewModel::class)
    abstract fun bindViewModel(viewModel: IntroViewModel): ViewModel
}
