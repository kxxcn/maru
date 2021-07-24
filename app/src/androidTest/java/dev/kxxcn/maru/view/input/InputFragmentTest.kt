package dev.kxxcn.maru.view.input

import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Account
import dev.kxxcn.maru.data.Task
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.data.source.FakeRepository
import dev.kxxcn.maru.util.KEY_TASK_ID
import dev.kxxcn.maru.util.monitorFragment
import dev.kxxcn.maru.view.base.BaseFragmentTest
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith
import java.text.NumberFormat
import java.util.*

@RunWith(AndroidJUnit4::class)
class InputFragmentTest : BaseFragmentTest() {

    @Test
    fun showEmptyTask() {
        val newTask = Task(name = "New Task 1")
        val newTaskDetail = TaskDetail().apply {
            task = newTask
        }
        runBlocking {
            (repository as? FakeRepository)?.addTaskDetails(newTaskDetail)
        }

        launchFragmentInContainer<InputFragment>(
            bundleOf(KEY_TASK_ID to newTask.id),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        val decimalFormat = NumberFormat.getInstance(Locale.KOREA)
        val expectedMoneyText = decimalFormat.format(0)

        onView(withId(R.id.input_husband_money)).check(matches(withText(expectedMoneyText)))
        onView(withId(R.id.input_wife_money)).check(matches(withText(expectedMoneyText)))
        onView(withId(R.id.input_remain_money)).check(matches(withText(expectedMoneyText)))
        onView(withId(R.id.input_task_money)).check(matches(withText(expectedMoneyText)))
    }

    @Test
    fun showEnteredTask() {
        val newTask = Task(name = "New Task 1")
        val newTaskDetail = TaskDetail().apply {
            task = newTask
            account = Account(
                husband = 500000,
                wife = 250000,
                remain = 1000000,
                date = System.currentTimeMillis(),
                taskId = newTask.id
            )
        }
        runBlocking {
            (repository as? FakeRepository)?.addTaskDetails(newTaskDetail)
        }

        launchFragmentInContainer<InputFragment>(
            bundleOf(KEY_TASK_ID to newTask.id),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        val decimalFormat = NumberFormat.getInstance(Locale.KOREA)
        val husbandMoney = newTaskDetail.account?.husband ?: 0
        val wifeMoney = newTaskDetail.account?.wife ?: 0
        val remainMoney = newTaskDetail.account?.remain ?: 0
        val totalMoney = husbandMoney + wifeMoney
        val expectedHusbandMoney = decimalFormat.format(husbandMoney)
        val expectedWifeMoney = decimalFormat.format(wifeMoney)
        val expectedRemainMoney = decimalFormat.format(remainMoney)
        val expectedTotalMoney = decimalFormat.format(totalMoney)

        onView(withId(R.id.input_husband_money)).check(matches(withText(expectedHusbandMoney)))
        onView(withId(R.id.input_wife_money)).check(matches(withText(expectedWifeMoney)))
        onView(withId(R.id.input_remain_money)).check(matches(withText(expectedRemainMoney)))
        onView(withId(R.id.input_task_money)).check(matches(withText(expectedTotalMoney)))
    }

    @Test
    fun clickInputButtonWhenNoData() {
        val newTask = Task(name = "New Task 1")
        runBlocking {
            (repository as? FakeRepository)?.let {
                it.addTaskDetails(TaskDetail().apply { task = newTask })
            }
        }

        launchFragmentInContainer<InputFragment>(
            bundleOf(KEY_TASK_ID to newTask.id),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(R.id.input_task_complete)).perform(scrollTo(), click())

        val expectedSnackbarText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.status_should_input_either)

        onView(withText(expectedSnackbarText)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

        onView(withId(R.id.input_progress)).check(matches(not(isDisplayed())))
    }

    @Test
    fun clickDecrementButtonWhenNoValueHasBeenEntered() {
        val newTask = Task(name = "New Task 1")
        runBlocking {
            (repository as? FakeRepository)?.let {
                it.addTaskDetails(TaskDetail().apply { task = newTask })
            }
        }

        launchFragmentInContainer<InputFragment>(
            bundleOf(KEY_TASK_ID to newTask.id),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        val expectedMoneyText = "0"

        onView(withId(R.id.input_husband_decrement)).perform(click())
        onView(withId(R.id.input_husband_money)).check(matches(withText(expectedMoneyText)))

        onView(withId(R.id.input_wife_decrement)).perform(click())
        onView(withId(R.id.input_wife_money)).check(matches(withText(expectedMoneyText)))

        onView(withId(R.id.input_remain_decrement)).perform(click())
        onView(withId(R.id.input_remain_money)).check(matches(withText(expectedMoneyText)))

        onView(withId(R.id.input_task_money)).check(matches(withText(expectedMoneyText)))
    }

    @Test
    fun clickDecrementButton() {
        val newTask = Task(name = "New Task 1")
        val newTaskDetail = TaskDetail().apply {
            task = newTask
            account = Account(
                husband = 50000000,
                wife = 40000000,
                remain = 30000000,
                date = System.currentTimeMillis(),
                taskId = newTask.id
            )
        }
        runBlocking {
            (repository as? FakeRepository)?.addTaskDetails(newTaskDetail)
        }

        launchFragmentInContainer<InputFragment>(
            bundleOf(KEY_TASK_ID to newTask.id),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        val decimalFormat = NumberFormat.getInstance(Locale.KOREA)

        var husbandMoney = newTaskDetail.account?.husband ?: 0
        var wifeMoney = newTaskDetail.account?.wife ?: 0
        var remainMoney = newTaskDetail.account?.remain ?: 0

        val tenThousand = 10000L
        val oneHundredThousand = 100000L
        val oneMillion = 1000000L
        val tenMillion = 10000000L

        // 10,000 단위 선택
        onView(withId(R.id.input_unit_ten_thousand)).perform(click())

        // 신랑 금액 감소 버튼 선택
        onView(withId(R.id.input_husband_decrement)).perform(click())

        // 신랑 금액 확인
        husbandMoney -= tenThousand
        var expectedHusbandMoney = decimalFormat.format(husbandMoney)
        onView(withId(R.id.input_husband_money)).check(matches(withText(expectedHusbandMoney)))

        // 100,000 단위 선택
        onView(withId(R.id.input_unit_one_hundred_thousand)).perform(click())

        // 신랑 금액 감소 버튼 선택
        onView(withId(R.id.input_husband_decrement)).perform(click())

        // 신랑 금액 확인
        husbandMoney -= oneHundredThousand
        expectedHusbandMoney = decimalFormat.format(husbandMoney)
        onView(withId(R.id.input_husband_money)).check(matches(withText(expectedHusbandMoney)))

        // 1,000,000 단위
        onView(withId(R.id.input_unit_one_million)).perform(click())

        // 신랑 금액 감소 버튼 선택
        onView(withId(R.id.input_husband_decrement)).perform(click())

        // 신랑 금액 확인
        husbandMoney -= oneMillion
        expectedHusbandMoney = decimalFormat.format(husbandMoney)
        onView(withId(R.id.input_husband_money)).check(matches(withText(expectedHusbandMoney)))

        // 10,000,000 단위 선택
        onView(withId(R.id.input_unit_ten_million)).perform(click())

        // 신랑 금액 감소 버튼 선택
        onView(withId(R.id.input_husband_decrement)).perform(click())

        // 신랑 금액 확인
        husbandMoney -= tenMillion
        expectedHusbandMoney = decimalFormat.format(husbandMoney)
        onView(withId(R.id.input_husband_money)).check(matches(withText(expectedHusbandMoney)))

        // 전체 금액 확인
        var totalMoney = husbandMoney + wifeMoney
        var expectedTotalMoney = decimalFormat.format(totalMoney)
        onView(withId(R.id.input_task_money)).check(matches(withText(expectedTotalMoney)))

        // 10,000 단위 선택
        onView(withId(R.id.input_unit_ten_thousand)).perform(click())

        // 신부 금액 감소 버튼 선택
        onView(withId(R.id.input_wife_decrement)).perform(click())

        // 신부 금액 확인
        wifeMoney -= tenThousand
        var expectedWifeMoney = decimalFormat.format(wifeMoney)
        onView(withId(R.id.input_wife_money)).check(matches(withText(expectedWifeMoney)))

        // 100,000 단위 선택
        onView(withId(R.id.input_unit_one_hundred_thousand)).perform(click())

        // 신부 금액 감소 버튼 선택
        onView(withId(R.id.input_wife_decrement)).perform(click())

        // 신부 금액 확인
        wifeMoney -= oneHundredThousand
        expectedWifeMoney = decimalFormat.format(wifeMoney)
        onView(withId(R.id.input_wife_money)).check(matches(withText(expectedWifeMoney)))

        // 1,000,000 단위
        onView(withId(R.id.input_unit_one_million)).perform(click())

        // 신부 금액 감소 버튼 선택
        onView(withId(R.id.input_wife_decrement)).perform(click())

        // 신부 금액 확인
        wifeMoney -= oneMillion
        expectedWifeMoney = decimalFormat.format(wifeMoney)
        onView(withId(R.id.input_wife_money)).check(matches(withText(expectedWifeMoney)))

        // 10,000,000 단위 선택
        onView(withId(R.id.input_unit_ten_million)).perform(click())

        // 신부 금액 감소 버튼 선택
        onView(withId(R.id.input_wife_decrement)).perform(click())

        // 신부 금액 확인
        wifeMoney -= tenMillion
        expectedWifeMoney = decimalFormat.format(wifeMoney)
        onView(withId(R.id.input_wife_money)).check(matches(withText(expectedWifeMoney)))

        // 전체 금액 확인
        totalMoney = husbandMoney + wifeMoney
        expectedTotalMoney = decimalFormat.format(totalMoney)
        onView(withId(R.id.input_task_money)).check(matches(withText(expectedTotalMoney)))

        // 10,000 단위 선택
        onView(withId(R.id.input_unit_ten_thousand)).perform(click())

        // 잔금 감소 버튼 선택
        onView(withId(R.id.input_remain_decrement)).perform(click())

        // 잔금 금액 확인
        remainMoney -= tenThousand
        var expectedRemainMoney = decimalFormat.format(remainMoney)
        onView(withId(R.id.input_remain_money)).check(matches(withText(expectedRemainMoney)))

        // 100,000 단위 선택
        onView(withId(R.id.input_unit_one_hundred_thousand)).perform(click())

        // 잔금 감소 버튼 선택
        onView(withId(R.id.input_remain_decrement)).perform(click())

        // 잔금 금액 확인
        remainMoney -= oneHundredThousand
        expectedRemainMoney = decimalFormat.format(remainMoney)
        onView(withId(R.id.input_remain_money)).check(matches(withText(expectedRemainMoney)))

        // 1,000,000 단위
        onView(withId(R.id.input_unit_one_million)).perform(click())

        // 잔금 감소 버튼 선택
        onView(withId(R.id.input_remain_decrement)).perform(click())

        // 잔금 금액 확인
        remainMoney -= oneMillion
        expectedRemainMoney = decimalFormat.format(remainMoney)
        onView(withId(R.id.input_remain_money)).check(matches(withText(expectedRemainMoney)))

        // 10,000,000 단위 선택
        onView(withId(R.id.input_unit_ten_million)).perform(click())

        // 잔금 감소 버튼 선택
        onView(withId(R.id.input_remain_decrement)).perform(click())

        // 잔금 금액 확인
        remainMoney -= tenMillion
        expectedRemainMoney = decimalFormat.format(remainMoney)
        onView(withId(R.id.input_remain_money)).check(matches(withText(expectedRemainMoney)))

        // 전체 금액 확인
        totalMoney = husbandMoney + wifeMoney
        expectedTotalMoney = decimalFormat.format(totalMoney)
        onView(withId(R.id.input_task_money)).check(matches(withText(expectedTotalMoney)))
    }

    @Test
    fun clickIncrementButton() {
        val newTask = Task(name = "New Task 1")
        val newTaskDetail = TaskDetail().apply {
            task = newTask
        }
        runBlocking {
            (repository as? FakeRepository)?.addTaskDetails(newTaskDetail)
        }

        launchFragmentInContainer<InputFragment>(
            bundleOf(KEY_TASK_ID to newTask.id),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        val decimalFormat = NumberFormat.getInstance(Locale.KOREA)

        var husbandMoney = newTaskDetail.account?.husband ?: 0
        var wifeMoney = newTaskDetail.account?.wife ?: 0
        var remainMoney = newTaskDetail.account?.remain ?: 0

        val tenThousand = 10000L
        val oneHundredThousand = 100000L
        val oneMillion = 1000000L
        val tenMillion = 10000000L

        // 10,000 단위 선택
        onView(withId(R.id.input_unit_ten_thousand)).perform(click())

        // 신랑 금액 증가 버튼 선택
        onView(withId(R.id.input_husband_increment)).perform(click())

        // 신랑 금액 확인
        husbandMoney += tenThousand
        var expectedHusbandMoney = decimalFormat.format(husbandMoney)
        onView(withId(R.id.input_husband_money)).check(matches(withText(expectedHusbandMoney)))

        // 100,000 단위 선택
        onView(withId(R.id.input_unit_one_hundred_thousand)).perform(click())

        // 신랑 금액 증가 버튼 선택
        onView(withId(R.id.input_husband_increment)).perform(click())

        // 신랑 금액 확인
        husbandMoney += oneHundredThousand
        expectedHusbandMoney = decimalFormat.format(husbandMoney)
        onView(withId(R.id.input_husband_money)).check(matches(withText(expectedHusbandMoney)))

        // 1,000,000 단위
        onView(withId(R.id.input_unit_one_million)).perform(click())

        // 신랑 금액 증가 버튼 선택
        onView(withId(R.id.input_husband_increment)).perform(click())

        // 신랑 금액 확인
        husbandMoney += oneMillion
        expectedHusbandMoney = decimalFormat.format(husbandMoney)
        onView(withId(R.id.input_husband_money)).check(matches(withText(expectedHusbandMoney)))

        // 10,000,000 단위 선택
        onView(withId(R.id.input_unit_ten_million)).perform(click())

        // 신랑 금액 증가 버튼 선택
        onView(withId(R.id.input_husband_increment)).perform(click())

        // 신랑 금액 확인
        husbandMoney += tenMillion
        expectedHusbandMoney = decimalFormat.format(husbandMoney)
        onView(withId(R.id.input_husband_money)).check(matches(withText(expectedHusbandMoney)))

        // 전체 금액 확인
        var totalMoney = husbandMoney + wifeMoney
        var expectedTotalMoney = decimalFormat.format(totalMoney)
        onView(withId(R.id.input_task_money)).check(matches(withText(expectedTotalMoney)))

        // 10,000 단위 선택
        onView(withId(R.id.input_unit_ten_thousand)).perform(click())

        // 신부 금액 증가 버튼 선택
        onView(withId(R.id.input_wife_increment)).perform(click())

        // 신부 금액 확인
        wifeMoney += tenThousand
        var expectedWifeMoney = decimalFormat.format(wifeMoney)
        onView(withId(R.id.input_wife_money)).check(matches(withText(expectedWifeMoney)))

        // 100,000 단위 선택
        onView(withId(R.id.input_unit_one_hundred_thousand)).perform(click())

        // 신부 금액 증가 버튼 선택
        onView(withId(R.id.input_wife_increment)).perform(click())

        // 신부 금액 확인
        wifeMoney += oneHundredThousand
        expectedWifeMoney = decimalFormat.format(wifeMoney)
        onView(withId(R.id.input_wife_money)).check(matches(withText(expectedWifeMoney)))

        // 1,000,000 단위
        onView(withId(R.id.input_unit_one_million)).perform(click())

        // 신부 금액 증가 버튼 선택
        onView(withId(R.id.input_wife_increment)).perform(click())

        // 신부 금액 확인
        wifeMoney += oneMillion
        expectedWifeMoney = decimalFormat.format(wifeMoney)
        onView(withId(R.id.input_wife_money)).check(matches(withText(expectedWifeMoney)))

        // 10,000,000 단위 선택
        onView(withId(R.id.input_unit_ten_million)).perform(click())

        // 신부 금액 증가 버튼 선택
        onView(withId(R.id.input_wife_increment)).perform(click())

        // 신부 금액 확인
        wifeMoney += tenMillion
        expectedWifeMoney = decimalFormat.format(wifeMoney)
        onView(withId(R.id.input_wife_money)).check(matches(withText(expectedWifeMoney)))

        // 전체 금액 확인
        totalMoney = husbandMoney + wifeMoney
        expectedTotalMoney = decimalFormat.format(totalMoney)
        onView(withId(R.id.input_task_money)).check(matches(withText(expectedTotalMoney)))

        // 10,000 단위 선택
        onView(withId(R.id.input_unit_ten_thousand)).perform(click())

        // 잔금 증가 버튼 선택
        onView(withId(R.id.input_remain_increment)).perform(click())

        // 잔금 금액 확인
        remainMoney += tenThousand
        var expectedRemainMoney = decimalFormat.format(remainMoney)
        onView(withId(R.id.input_remain_money)).check(matches(withText(expectedRemainMoney)))

        // 100,000 단위 선택
        onView(withId(R.id.input_unit_one_hundred_thousand)).perform(click())

        // 잔금 증가 버튼 선택
        onView(withId(R.id.input_remain_increment)).perform(click())

        // 잔금 금액 확인
        remainMoney += oneHundredThousand
        expectedRemainMoney = decimalFormat.format(remainMoney)
        onView(withId(R.id.input_remain_money)).check(matches(withText(expectedRemainMoney)))

        // 1,000,000 단위
        onView(withId(R.id.input_unit_one_million)).perform(click())

        // 잔금 증가 버튼 선택
        onView(withId(R.id.input_remain_increment)).perform(click())

        // 잔금 금액 확인
        remainMoney += oneMillion
        expectedRemainMoney = decimalFormat.format(remainMoney)
        onView(withId(R.id.input_remain_money)).check(matches(withText(expectedRemainMoney)))

        // 10,000,000 단위 선택
        onView(withId(R.id.input_unit_ten_million)).perform(click())

        // 잔금 증가 버튼 선택
        onView(withId(R.id.input_remain_increment)).perform(click())

        // 잔금 금액 확인
        remainMoney += tenMillion
        expectedRemainMoney = decimalFormat.format(remainMoney)
        onView(withId(R.id.input_remain_money)).check(matches(withText(expectedRemainMoney)))

        // 전체 금액 확인
        totalMoney = husbandMoney + wifeMoney
        expectedTotalMoney = decimalFormat.format(totalMoney)
        onView(withId(R.id.input_task_money)).check(matches(withText(expectedTotalMoney)))
    }
}
