package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.days.DaysAddFragment
import dev.kxxcn.maru.view.days.DaysAddViewModel

@Module
abstract class DaysAddModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun daysAddFragment(): DaysAddFragment

    @Binds
    @IntoMap
    @ViewModelKey(DaysAddViewModel::class)
    abstract fun bindViewModel(viewModel: DaysAddViewModel): ViewModel
}
