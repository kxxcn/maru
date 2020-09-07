package dev.kxxcn.maru.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.kxxcn.maru.view.order.OrderFragment

@Module
abstract class OrderModule {

    @ContributesAndroidInjector
    internal abstract fun orderFragment(): OrderFragment
}
