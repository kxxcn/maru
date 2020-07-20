package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.register.RegisterFragment
import dev.kxxcn.maru.view.register.RegisterViewModel

@Module
abstract class RegisterModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun registerFragment(): RegisterFragment

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindViewModel(viewModel: RegisterViewModel): ViewModel
}
