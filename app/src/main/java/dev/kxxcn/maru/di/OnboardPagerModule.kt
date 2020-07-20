package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.onboard.page.OnboardPagerFragment
import dev.kxxcn.maru.view.onboard.page.OnboardPagerViewModel

@Module
abstract class OnboardPagerModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun onboardPagerFragment(): OnboardPagerFragment

    @Binds
    @IntoMap
    @ViewModelKey(OnboardPagerViewModel::class)
    abstract fun bindViewModel(viewModel: OnboardPagerViewModel): ViewModel
}
