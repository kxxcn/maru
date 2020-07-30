package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.more.MoreFragment
import dev.kxxcn.maru.view.more.MoreViewModel

@Module
abstract class MoreModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class,
            AuthenticationModule::class
        ]
    )
    internal abstract fun moreFragment(): MoreFragment

    @Binds
    @IntoMap
    @ViewModelKey(MoreViewModel::class)
    abstract fun bindViewModel(viewModel: MoreViewModel): ViewModel
}
