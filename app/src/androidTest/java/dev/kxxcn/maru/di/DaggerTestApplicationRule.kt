package dev.kxxcn.maru.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class DaggerTestApplicationRule : TestWatcher() {

    lateinit var component: TestApplicationComponent
        private set

    override fun starting(description: Description?) {
        super.starting(description)
        val app = ApplicationProvider.getApplicationContext<Context>() as TestMaruApplication

        component = DaggerTestApplicationComponent.factory().create(app)
        component.inject(app)
    }
}
