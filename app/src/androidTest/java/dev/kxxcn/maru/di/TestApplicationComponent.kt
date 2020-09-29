package dev.kxxcn.maru.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dev.kxxcn.maru.data.source.DataRepository
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AssistedInjectModule::class,
        TestApplicationModule::class,
        TasksModule::class,
        InputModule::class,
        HomeModule::class
    ]
)
interface TestApplicationComponent : AndroidInjector<TestMaruApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: TestMaruApplication): TestApplicationComponent
    }

    val maruRepository: DataRepository
}
