package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.edit.EditFragment
import dev.kxxcn.maru.view.edit.EditViewModel

@Module
abstract class EditModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun editFragment(): EditFragment

    @Binds
    @IntoMap
    @ViewModelKey(EditViewModel::class)
    abstract fun bindViewModel(viewModel: EditViewModel): ViewModel
}
