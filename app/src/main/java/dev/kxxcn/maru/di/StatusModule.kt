package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.status.StatusFragment
import dev.kxxcn.maru.view.status.StatusViewModel

@Module
abstract class StatusModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun statusFragment(): StatusFragment

    @Binds
    @IntoMap
    @ViewModelKey(StatusViewModel::class)
    abstract fun bindViewModel(viewModel: StatusViewModel): ViewModel
}
