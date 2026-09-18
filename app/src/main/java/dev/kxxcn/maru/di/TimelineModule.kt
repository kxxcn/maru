package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.timeline.TimelineFragment
import dev.kxxcn.maru.view.timeline.TimelineViewModel

@Module
abstract class TimelineModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun timelineFragment(): TimelineFragment

    @Binds
    @IntoMap
    @ViewModelKey(TimelineViewModel::class)
    abstract fun bindViewModel(viewModel: TimelineViewModel): ViewModel
}
