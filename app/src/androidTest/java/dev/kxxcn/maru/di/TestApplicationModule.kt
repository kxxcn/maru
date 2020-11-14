package dev.kxxcn.maru.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.source.FakeRepository
import dev.kxxcn.maru.util.AdHelper
import javax.inject.Singleton

@Module
class TestApplicationModule {

    @Singleton
    @Provides
    fun provideRepository(): DataRepository {
        return FakeRepository()
    }

    @Singleton
    @Provides
    fun provideAnalytics(application: TestMaruApplication): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideAdHelper(application: TestMaruApplication): AdHelper {
        return AdHelper(application.applicationContext)
    }
}
