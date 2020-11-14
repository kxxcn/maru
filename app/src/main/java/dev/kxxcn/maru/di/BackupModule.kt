package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.backup.BackupFragment
import dev.kxxcn.maru.view.backup.BackupViewModel

@Module
abstract class BackupModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun backupFragment(): BackupFragment

    @Binds
    @IntoMap
    @ViewModelKey(BackupViewModel::class)
    abstract fun bindViewModel(factory: BackupViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
