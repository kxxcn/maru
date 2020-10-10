package dev.kxxcn.maru.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.kxxcn.maru.view.present.PresentFragment

@Module
abstract class PresentModule {

    @ContributesAndroidInjector
    internal abstract fun presentModule(): PresentFragment
}
