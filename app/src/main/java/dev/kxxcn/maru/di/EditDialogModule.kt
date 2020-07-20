package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.edit.EditDialogFragment
import dev.kxxcn.maru.view.edit.EditDialogViewModel

@Module
abstract class EditDialogModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun editDialogFragment(): EditDialogFragment

    @Binds
    @IntoMap
    @ViewModelKey(EditDialogViewModel::class)
    abstract fun bindViewModel(factory: EditDialogViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
