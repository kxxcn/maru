package dev.kxxcn.maru.di

import dagger.Module
import dagger.Provides
import dev.kxxcn.maru.MaruApplication
import dev.kxxcn.maru.util.BillingManager
import dev.kxxcn.maru.view.base.BaseCoroutineScope

@Module
internal abstract class BillingModule {

    companion object {

        @Provides
        fun provideBillingManager(
            application: MaruApplication,
            scope: BaseCoroutineScope
        ): BillingManager {
            return BillingManager(application.applicationContext, scope)
        }
    }
}
