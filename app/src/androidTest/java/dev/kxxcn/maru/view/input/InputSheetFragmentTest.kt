package dev.kxxcn.maru.view.input

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Account
import dev.kxxcn.maru.data.Task
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.data.source.FakeRepository
import dev.kxxcn.maru.util.KEY_TASK_ID
import dev.kxxcn.maru.view.base.BaseFragmentTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import java.text.NumberFormat
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class InputSheetFragmentTest : BaseFragmentTest() {

    private val decimalFormat = NumberFormat.getInstance(Locale.KOREA)

    private fun launch(taskId: String) {
        launchFragment<InputSheetFragment>(
            bundleOf(KEY_TASK_ID to taskId),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }
    }

    @Test
    fun showEmptyTask() {
        val newTask = Task(name = "New Task 1")
        runBlocking { (repository as? FakeRepository)?.addTaskDetails(TaskDetail().apply { task = newTask }) }

        launch(newTask.id)

        val zero = decimalFormat.format(0)
        onView(withId(R.id.input_husband_money)).inRoot(isDialog()).check(matches(withText(zero)))
        onView(withId(R.id.input_wife_money)).inRoot(isDialog()).check(matches(withText(zero)))
        onView(withId(R.id.input_remain_money)).inRoot(isDialog()).check(matches(withText(zero)))
        onView(withId(R.id.input_task_money)).inRoot(isDialog()).check(matches(withText(zero)))
    }

    @Test
    fun showEnteredTask() {
        val newTask = Task(name = "New Task 1")
        val detail = TaskDetail().apply {
            task = newTask
            account = Account(husband = 500000, wife = 250000, remain = 1000000, date = System.currentTimeMillis(), taskId = newTask.id)
        }
        runBlocking { (repository as? FakeRepository)?.addTaskDetails(detail) }

        launch(newTask.id)

        onView(withId(R.id.input_husband_money)).inRoot(isDialog()).check(matches(withText(decimalFormat.format(500000))))
        onView(withId(R.id.input_wife_money)).inRoot(isDialog()).check(matches(withText(decimalFormat.format(250000))))
        onView(withId(R.id.input_remain_money)).inRoot(isDialog()).check(matches(withText(decimalFormat.format(1000000))))
        onView(withId(R.id.input_task_money)).inRoot(isDialog()).check(matches(withText(decimalFormat.format(750000))))
    }
}
