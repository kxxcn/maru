package dev.kxxcn.maru.view.tasks

import android.content.Context
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Task
import dev.kxxcn.maru.util.RecyclerViewMatcher
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.BaseFragmentTest
import dev.kxxcn.maru.view.tasks.holder.TasksEmptyHolder
import dev.kxxcn.maru.view.tasks.holder.TasksNativeAdHolder
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksFragmentTest : BaseFragmentTest() {

    private fun launch(filterType: TasksFilterType) {
        PreferenceUtils.taskFilterType = filterType.ordinal
        launchFragmentInContainer<TasksFragment>(
            themeResId = R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }
    }

    private fun replaceTasks(vararg tasks: Task) {
        runBlocking { repository.replaceTasks(tasks.toList()) }
    }

    @Test
    fun tasksFragmentScreenDisplayedInUI() {
        launch(TasksFilterType.ALL_TASKS)

        val expectedTitle = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.menu_tasks)

        onView(withId(R.id.tasks_title))
            .check(matches(isDisplayed()))
            .check(matches(withText(expectedTitle)))
        onView(withId(R.id.tasks_subtitle)).check(matches(isDisplayed()))
        onView(withId(R.id.tasks_segment)).check(matches(isDisplayed()))
        onView(withId(R.id.tasks_add)).check(matches(isDisplayed()))
    }

    @Test
    fun showEmptyTasks() {
        launch(TasksFilterType.COMPLETED_TASKS)

        onView(withId(R.id.tasks_list)).check(
            matches(RecyclerViewMatcher.matchHolder<TasksEmptyHolder>(0))
        )
    }

    @Test
    fun showAllTasks() {
        val newTask1 = Task(name = "New Task 1", priority = 0)
        val newTask2 = Task(name = "New Task 2", priority = 1)
        val newTask3 = Task(name = "New Task 3", priority = 2, isCompleted = true)
        replaceTasks(newTask1, newTask2, newTask3)

        launch(TasksFilterType.ALL_TASKS)

        onView(withId(R.id.tasks_list))
            .check(matches(RecyclerViewMatcher.atPosition(0, hasDescendant(withText(newTask1.name)))))
            .check(matches(RecyclerViewMatcher.atPosition(2, hasDescendant(withText(newTask3.name)))))
    }

    @Test
    fun showProgressTasks() {
        val newTask1 = Task(name = "New Task 1", priority = 0, isCompleted = true)
        val newTask2 = Task(name = "New Task 2", priority = 1, isCompleted = true)
        val newTask3 = Task(name = "New Task 3", priority = 2)
        replaceTasks(newTask1, newTask2, newTask3)

        launch(TasksFilterType.ACTIVE_TASKS)

        onView(withId(R.id.tasks_list))
            .check(matches(RecyclerViewMatcher.atPosition(0, hasDescendant(withText(newTask3.name)))))
    }

    @Test
    fun showCompletedTasks() {
        val newTask1 = Task(name = "New Task 1", priority = 0)
        val newTask2 = Task(name = "New Task 2", priority = 1)
        val newTask3 = Task(name = "New Task 3", priority = 2, isCompleted = true)
        replaceTasks(newTask1, newTask2, newTask3)

        launch(TasksFilterType.COMPLETED_TASKS)

        onView(withId(R.id.tasks_list))
            .check(matches(RecyclerViewMatcher.atPosition(0, hasDescendant(withText(newTask3.name)))))
    }

    @Test
    fun noAdvertisementBeforeFifthRow() {
        val tasks = (1..4).map { Task(name = "New Task $it", priority = it.toLong()) }
        replaceTasks(*tasks.toTypedArray())

        launch(TasksFilterType.ALL_TASKS)

        for (position in 0 until 4) {
            onView(withId(R.id.tasks_list)).check(
                matches(not(RecyclerViewMatcher.matchHolder<TasksNativeAdHolder>(position)))
            )
        }
    }
}
