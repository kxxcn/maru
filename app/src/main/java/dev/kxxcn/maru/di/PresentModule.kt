package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.present.PresentFragment
import dev.kxxcn.maru.view.present.PresentViewModel

@Module
abstract class PresentModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun presentFragment(): PresentFragment

    @Binds
    @IntoMap
    @ViewModelKey(PresentViewModel::class)
    abstract fun bindViewModel(factory: PresentViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
