package dev.kxxcn.maru.di

import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.Module
import dagger.Provides
import dev.kxxcn.maru.MaruApplication

@Module
internal abstract class ReviewManagerModule {

    companion object {

        @Provides
        fun provideReviewManager(application: MaruApplication): ReviewManager {
            return ReviewManagerFactory.create(application.applicationContext)
        }
    }
}
