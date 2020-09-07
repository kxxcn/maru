package dev.kxxcn.maru.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.kxxcn.maru.view.timeline.TimelineFragment

@Module
abstract class TimelineModule {

    @ContributesAndroidInjector
    internal abstract fun timelineFragment(): TimelineFragment
}
