package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.landmark.LandmarkFragment
import dev.kxxcn.maru.view.landmark.LandmarkViewModel

@Module
abstract class LandmarkModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun landmarkFragment(): LandmarkFragment

    @Binds
    @IntoMap
    @ViewModelKey(LandmarkViewModel::class)
    abstract fun bindViewModel(viewModel: LandmarkViewModel): ViewModel
}
