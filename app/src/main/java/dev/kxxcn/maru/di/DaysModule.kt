package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.days.DaysFragment
import dev.kxxcn.maru.view.days.DaysViewModel

@Module
abstract class DaysModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun daysFragment(): DaysFragment

    @Binds
    @IntoMap
    @ViewModelKey(DaysViewModel::class)
    abstract fun bindViewModel(viewModel: DaysViewModel): ViewModel
}
