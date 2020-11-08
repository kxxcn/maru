package dev.kxxcn.maru.view.home

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.User
import dev.kxxcn.maru.data.source.FakeRepository
import dev.kxxcn.maru.util.ConvertUtils
import dev.kxxcn.maru.util.RecyclerViewMatcher
import dev.kxxcn.maru.util.monitorFragment
import dev.kxxcn.maru.util.scrollToBottom
import dev.kxxcn.maru.view.base.BaseFragmentTest
import dev.kxxcn.maru.view.home.holder.BannerAdHolder
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest : BaseFragmentTest() {

    private val parentId = R.id.contents_list

    @Test
    fun showUserName() {
        val name = runBlocking {
            val result = repository.getUsers()
            if (result is Success) {
                result.data.first().name
            } else {
                ""
            }
        }

        val expectedUserNameText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.home_welcome_card_name, name)

        launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(
            RecyclerViewMatcher.atPositionOnView(
                HomeAdapter.TYPE_WELCOME,
                parentId,
                R.id.welcome_name_text
            )
        ).check(matches(withText(expectedUserNameText)))
    }

    @Test
    fun showVerifiedBadge() {
        launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(
            RecyclerViewMatcher.atPositionOnView(
                HomeAdapter.TYPE_WELCOME,
                parentId,
                R.id.welcome_verified
            )
        ).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun showRemainingWeddingDate() {
        val date = runBlocking {
            val result = repository.getUsers()
            if (result is Success) {
                ConvertUtils.computeRemain(result.data.first().wedding) ?: 0L
            } else {
                0L
            }
        }

        val expectedWeddingDateText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.home_welcome_card_remain2, date)

        launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(
            RecyclerViewMatcher.atPositionOnView(
                HomeAdapter.TYPE_WELCOME,
                parentId,
                R.id.welcome_day_remain
            )
        ).check(matches(withText(expectedWeddingDateText)))
    }

    @Test
    fun showDateOfTheWeddingThatHasPassed() {
        runBlocking {
            val wedding = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5)
            val user = User(name = "New", budget = 50000000L, wedding = wedding)
            (repository as? FakeRepository)?.replaceUser(user)
        }

        val expectedWeddingDateText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.congratulations)

        launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(
            RecyclerViewMatcher.atPositionOnView(
                HomeAdapter.TYPE_WELCOME,
                parentId,
                R.id.welcome_day_remain
            )
        ).check(matches(withText(expectedWeddingDateText)))
    }

    @Test
    fun accountCardDisplayedInUI() {
        val summary = runBlocking {
            val result = repository.getSummary()
            if (result is Success) {
                result.data.first()
            } else {
                throw RuntimeException("Invalid Data.")
            }
        }
        launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        val budget = summary.user?.budget ?: 0L

        val expectedTotalAccountsText = summary.totalAccountsText
        val expectedHusbandAccountsText = ConvertUtils.moneyText(summary.husbandAccounts)
        val expectedWifeAccountsText = ConvertUtils.moneyText(summary.wifeAccounts)
        val expectedTotalBudgetText = ConvertUtils.moneyText(budget)
        val expectedTotalRemainText = ConvertUtils.moneyText(budget - summary.totalAccounts)

        onView(
            RecyclerViewMatcher.atPositionOnView(
                HomeAdapter.TYPE_ACCOUNT,
                parentId,
                R.id.total_accounts_text
            )
        ).check(matches(withText(expectedTotalAccountsText)))

        onView(
            RecyclerViewMatcher.atPositionOnView(
                HomeAdapter.TYPE_ACCOUNT,
                parentId,
                R.id.husband_accounts_text
            )
        ).check(matches(withText(expectedHusbandAccountsText)))

        onView(
            RecyclerViewMatcher.atPositionOnView(
                HomeAdapter.TYPE_ACCOUNT,
                parentId,
                R.id.wife_accounts_text
            )
        ).check(matches(withText(expectedWifeAccountsText)))

        onView(
            RecyclerViewMatcher.atPositionOnView(
                HomeAdapter.TYPE_ACCOUNT,
                parentId,
                R.id.total_budget_text
            )
        ).check(matches(withText(expectedTotalBudgetText)))

        onView(
            RecyclerViewMatcher.atPositionOnView(
                HomeAdapter.TYPE_ACCOUNT,
                parentId,
                R.id.total_remain_text
            )
        ).check(matches(withText(expectedTotalRemainText)))
    }

    @Test
    fun showAdvertisement() {
        launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(parentId)).perform(scrollToBottom())

        onView(withId(parentId))
            .check(
                matches(
                    RecyclerViewMatcher.matchHolder<BannerAdHolder>(HomeAdapter.TYPE_BANNER_AD)
                )
            )
    }

    @Test
    fun doNotShowAdvertisementWhenPremiumUser() {
        runBlocking {
            (repository as? FakeRepository)?.replaceUser(User(premium = true))
        }
        launchFragmentInContainer<HomeFragment>(
            themeResId = R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(parentId)).perform(scrollToBottom())

        onView(withId(parentId))
            .check(
                matches(
                    not(
                        RecyclerViewMatcher.matchHolder<BannerAdHolder>(HomeAdapter.TYPE_BANNER_AD)
                    )
                )
            )
    }
}
