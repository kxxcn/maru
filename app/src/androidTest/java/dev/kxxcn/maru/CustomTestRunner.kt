package dev.kxxcn.maru

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dev.kxxcn.maru.di.TestMaruApplication

class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, TestMaruApplication::class.java.name, context)
    }
}
