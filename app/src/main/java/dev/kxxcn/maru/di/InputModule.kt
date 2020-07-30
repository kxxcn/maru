package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.input.InputFragment
import dev.kxxcn.maru.view.input.InputViewModel

@Module
abstract class InputModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun inputFragment(): InputFragment

    @Binds
    @IntoMap
    @ViewModelKey(InputViewModel::class)
    abstract fun bindViewModel(factory: InputViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
