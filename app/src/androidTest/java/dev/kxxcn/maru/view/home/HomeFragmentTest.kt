package dev.kxxcn.maru.view.home

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.BaseFragmentTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest : BaseFragmentTest() {

    @Test
    fun homeShowsDdayCardAndBudget() {
        PreferenceUtils.shownOnboard = true
        launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(R.id.home_dday_card)).check(matches(isDisplayed()))
        onView(withId(R.id.home_budget_used)).check(matches(isDisplayed()))
    }
}
