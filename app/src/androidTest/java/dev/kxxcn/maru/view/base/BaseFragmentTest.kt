package dev.kxxcn.maru.view.base

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.di.DaggerTestApplicationRule
import dev.kxxcn.maru.util.DataBindingIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

open class BaseFragmentTest {

    protected val dataBindingIdlingResource = DataBindingIdlingResource()

    protected lateinit var repository: DataRepository

    @get:Rule
    val rule = DaggerTestApplicationRule()

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Before
    fun setupRepository() {
        repository = rule.component.maruRepository
    }
}
