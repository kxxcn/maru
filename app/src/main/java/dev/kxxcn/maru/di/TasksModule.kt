package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.tasks.TasksFragment
import dev.kxxcn.maru.view.tasks.TasksViewModel

@Module
abstract class TasksModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun tasksFragment(): TasksFragment

    @Binds
    @IntoMap
    @ViewModelKey(TasksViewModel::class)
    abstract fun bindViewModel(viewModel: TasksViewModel): ViewModel
}
