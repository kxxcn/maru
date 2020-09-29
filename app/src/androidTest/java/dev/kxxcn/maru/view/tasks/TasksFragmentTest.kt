package dev.kxxcn.maru.view.tasks

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Task
import dev.kxxcn.maru.util.RecyclerViewMatcher
import dev.kxxcn.maru.util.RotateSelectionViewMatcher
import dev.kxxcn.maru.util.monitorFragment
import dev.kxxcn.maru.view.base.BaseFragmentTest
import dev.kxxcn.maru.view.tasks.holder.TasksEmptyHolder
import dev.kxxcn.maru.view.tasks.holder.TasksNativeAdHolder
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class TasksFragmentTest : BaseFragmentTest() {

    @Test
    fun tasksFragmentScreenDisplayedInUI() {
        launchFragmentInContainer<TasksFragment>(
            Bundle(),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        val expectedTasksFragmentTitleText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.menu_tasks)
        val expectedTotalButtonText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.tasks_total_desc)
        val expectedProgressButtonText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.tasks_progress_desc)
        val expectedCompletedButtonText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.tasks_completed_desc)

        onView(withId(R.id.tasks_title))
            .check(matches(isDisplayed()))
            .check(matches(withText(expectedTasksFragmentTitleText)))

        onView(withId(R.id.tasks_total_desc))
            .check(matches(isDisplayed()))
            .check(matches(RotateSelectionViewMatcher.withText(expectedTotalButtonText)))

        onView(withId(R.id.tasks_progress_desc))
            .check(matches(isDisplayed()))
            .check(matches(RotateSelectionViewMatcher.withText(expectedProgressButtonText)))

        onView(withId(R.id.tasks_completed_desc))
            .check(matches(isDisplayed()))
            .check(matches(RotateSelectionViewMatcher.withText(expectedCompletedButtonText)))
    }

    @Test
    fun showEmptyTasks() {
        launchFragmentInContainer<TasksFragment>(
            Bundle(),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(R.id.tasks_completed_desc)).perform(click())
        onView(withId(R.id.tasks_list)).check(
            matches(
                RecyclerViewMatcher.matchHolder<TasksEmptyHolder>(0)
            )
        )
    }

    @Test
    fun showAllTasks() {
        val newTask1 = Task(name = "New Task 1")
        val newTask2 = Task(name = "New Task 2")
        val newTask3 = Task(name = "New Task 3", isCompleted = true)
        val newTask4 = Task(name = "New Task 4")
        val newTask5 = Task(name = "New Task 5", isCompleted = true)
        runBlocking {
            repository.replaceTasks(
                listOf(
                    newTask1,
                    newTask2,
                    newTask3,
                    newTask4,
                    newTask5
                )
            )
        }

        launchFragmentInContainer<TasksFragment>(
            Bundle(),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(R.id.tasks_total_desc)).perform(click())
        onView(withId(R.id.tasks_list))
            .check(
                matches(
                    RecyclerViewMatcher.atPosition(
                        0,
                        hasDescendant(withText(newTask1.name))
                    )
                )
            )
    }

    @Test
    fun showProgressTasks() {
        val newTask1 = Task(name = "New Task 1", isCompleted = true)
        val newTask2 = Task(name = "New Task 2", isCompleted = true)
        val newTask3 = Task(name = "New Task 3")
        val newTask4 = Task(name = "New Task 4")
        val newTask5 = Task(name = "New Task 5")
        runBlocking {
            repository.replaceTasks(
                listOf(
                    newTask1,
                    newTask2,
                    newTask3,
                    newTask4,
                    newTask5
                )
            )
        }

        launchFragmentInContainer<TasksFragment>(
            Bundle(),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(R.id.tasks_progress_desc)).perform(click())
        onView(withId(R.id.tasks_list))
            .check(
                matches(
                    RecyclerViewMatcher.atPosition(
                        0,
                        hasDescendant(withText(newTask3.name))
                    )
                )
            )
    }

    @Test
    fun showCompletedTasks() {
        val newTask1 = Task(name = "New Task 1")
        val newTask2 = Task(name = "New Task 2")
        val newTask3 = Task(name = "New Task 3", isCompleted = true)
        val newTask4 = Task(name = "New Task 4")
        val newTask5 = Task(name = "New Task 5")
        runBlocking {
            repository.replaceTasks(
                listOf(
                    newTask1,
                    newTask2,
                    newTask3,
                    newTask4,
                    newTask5
                )
            )
        }

        launchFragmentInContainer<TasksFragment>(
            Bundle(),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(R.id.tasks_completed_desc)).perform(click())
        onView(withId(R.id.tasks_list))
            .check(
                matches(
                    RecyclerViewMatcher.atPosition(
                        0,
                        hasDescendant(withText(newTask3.name))
                    )
                )
            )
    }

    @Test
    fun showNativeAdvertisement() {
        launchFragmentInContainer<TasksFragment>(
            Bundle(),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(R.id.tasks_total_desc)).perform(click())
        onView(withId(R.id.tasks_list)).check(
            matches(
                not(
                    RecyclerViewMatcher.matchHolder<TasksNativeAdHolder>(0)
                )
            )
        )
        onView(withId(R.id.tasks_list)).check(
            matches(
                RecyclerViewMatcher.matchHolder<TasksNativeAdHolder>(1)
            )
        )
    }

    @Test
    fun clickEditButtonNavigateToEditFragment() {
        val navController = mock(NavController::class.java)

        launchFragmentInContainer<TasksFragment>(
            Bundle(),
            R.style.AppTheme
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

        onView(withId(R.id.tasks_edit)).perform(click())
        verify(navController).navigate(TasksFragmentDirections.actionTasksFragmentToEditFragment())
    }

    @Test
    fun clickTaskItemNavigateToInputFragment() {
        val newTask1 = Task(name = "New Task 1")
        val newTask2 = Task(name = "New Task 2")
        val newTask3 = Task(name = "New Task 3")
        runBlocking {
            repository.replaceTasks(
                listOf(
                    newTask1,
                    newTask2,
                    newTask3
                )
            )
        }

        val navController = mock(NavController::class.java)

        launchFragmentInContainer<TasksFragment>(
            Bundle(),
            R.style.AppTheme
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

        onView(withId(R.id.tasks_progress_desc)).perform(click())
        onView(withId(R.id.tasks_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        verify(navController).navigate(
            TasksFragmentDirections.actionTasksFragmentToInputFragment(
                newTask1.id,
                false
            )
        )
    }
}
