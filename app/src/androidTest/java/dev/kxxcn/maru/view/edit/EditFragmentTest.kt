package dev.kxxcn.maru.view.edit

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.User
import dev.kxxcn.maru.data.source.FakeRepository
import dev.kxxcn.maru.util.ConvertUtils
import dev.kxxcn.maru.util.monitorFragment
import dev.kxxcn.maru.view.base.BaseFragmentTest
import dev.kxxcn.maru.view.register.RegisterFilterType.REGISTER_BUDGET
import dev.kxxcn.maru.view.register.RegisterFilterType.REGISTER_NAME
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class EditFragmentTest : BaseFragmentTest() {

    @Test
    fun showUserInfo() {
        val expectedUserNameText = "New"
        val budget = 50000000L
        val expectedUserBudgetText = ConvertUtils.moneyFormat(budget)

        runBlocking {
            val wedding = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5)
            val user = User(name = expectedUserNameText, budget = budget, wedding = wedding)
            (repository as? FakeRepository)?.replaceUser(user)
        }

        launchFragmentInContainer<EditFragment>(
            themeResId = R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(R.id.edit_name)).check(matches(withText(expectedUserNameText)))
        onView(withId(R.id.edit_budget)).check(matches(withText(expectedUserBudgetText)))
    }

    @Test
    fun clickUserNameNavigateToEditDialogFragment() {
        val navController = Mockito.mock(NavController::class.java)

        runBlocking {
            val wedding = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5)
            val user = User(name = "New", budget = 50000000L, wedding = wedding)
            (repository as? FakeRepository)?.replaceUser(user)
        }

        launchFragmentInContainer<EditFragment>(
            themeResId = R.style.AppTheme
        ).apply {
            onFragment {
                it.view?.let { view ->
                    Navigation.setViewNavController(
                        view,
                        navController
                    )
                }
            }
        }.also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(R.id.edit_name_parent)).perform(click())
        verify(navController).navigate(
            EditFragmentDirections.actionEditFragmentToEditDialogFragment(
                REGISTER_NAME
            )
        )
    }

    @Test
    fun clickUserBudgetNavigateToEditDialogFragment() {
        val navController = Mockito.mock(NavController::class.java)

        runBlocking {
            val wedding = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5)
            val user = User(name = "New", budget = 50000000L, wedding = wedding)
            (repository as? FakeRepository)?.replaceUser(user)
        }

        launchFragmentInContainer<EditFragment>(
            themeResId = R.style.AppTheme
        ).apply {
            onFragment {
                it.view?.let { view ->
                    Navigation.setViewNavController(
                        view,
                        navController
                    )
                }
            }
        }.also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(R.id.edit_budget_parent)).perform(click())
        verify(navController).navigate(
            EditFragmentDirections.actionEditFragmentToEditDialogFragment(
                REGISTER_BUDGET
            )
        )
    }
}
