package dev.kxxcn.maru.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dev.kxxcn.maru.MaruApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AssistedInjectModule::class,
        ApplicationModule::class,
        HomeModule::class,
        PresentModule::class,
        SplashModule::class,
        RegisterModule::class,
        OnboardPagerModule::class,
        TasksModule::class,
        InputModule::class,
        StatusModule::class,
        SortModule::class,
        LandmarkModule::class,
        DaysModule::class,
        DaysAddModule::class,
        NoticeModule::class,
        EditModule::class,
        EditDialogModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<MaruApplication> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<MaruApplication>
}
