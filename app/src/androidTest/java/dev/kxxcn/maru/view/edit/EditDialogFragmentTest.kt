package dev.kxxcn.maru.view.edit

import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Task
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.data.source.FakeRepository
import dev.kxxcn.maru.util.KEY_REGISTER_TYPE
import dev.kxxcn.maru.util.KEY_TASK_ID
import dev.kxxcn.maru.util.monitorFragment
import dev.kxxcn.maru.view.base.BaseFragmentTest
import dev.kxxcn.maru.view.register.RegisterFilterType.REGISTER_BUDGET
import dev.kxxcn.maru.view.register.RegisterFilterType.REGISTER_NAME
import dev.kxxcn.maru.view.register.RegisterFilterType.REGISTER_TASK_EDIT
import dev.kxxcn.maru.view.register.RegisterFilterType.REGISTER_WEDDING
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditDialogFragmentTest : BaseFragmentTest() {

    @Test
    fun nameEditorDisplayedInUI() {
        launchFragmentInContainer<EditDialogFragment>(
            bundleOf(KEY_REGISTER_TYPE to REGISTER_NAME),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        val expectedNameTitleText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.edit_dialog_title_name)

        val expectedNameHintText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.edit_dialog_title_name_hint)

        onView(withId(R.id.edit_title)).check(matches(withText(expectedNameTitleText)))
        onView(withId(R.id.edit_input)).check(matches(withHint(expectedNameHintText)))
    }

    @Test
    fun budgetEditorDisplayedInUI() {
        launchFragmentInContainer<EditDialogFragment>(
            bundleOf(KEY_REGISTER_TYPE to REGISTER_BUDGET),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        val expectedBudgetTitleText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.edit_dialog_title_budget)

        val expectedBudgetHintText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.edit_dialog_title_budget_hint)

        onView(withId(R.id.edit_title)).check(matches(withText(expectedBudgetTitleText)))
        onView(withId(R.id.edit_input)).check(matches(withHint(expectedBudgetHintText)))
    }

    @Test
    fun weddingEditorDisplayedInUI() {
        launchFragmentInContainer<EditDialogFragment>(
            bundleOf(KEY_REGISTER_TYPE to REGISTER_WEDDING),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        val expectedTitleText = ApplicationProvider
            .getApplicationContext<Context>()
            .getString(R.string.edit_dialog_title_wedding)

        onView(withId(R.id.edit_title)).check(matches(withText(expectedTitleText)))
        onView(withId(R.id.edit_input)).check(matches(isDisplayed()))
    }

    @Test
    fun taskEditorDisplaysExistingName() {
        val task = Task(name = "기존 체크리스트")
        runBlocking {
            (repository as FakeRepository).replaceTasks(listOf(task))
            (repository as FakeRepository).addTaskDetails(
                TaskDetail().apply { this.task = task }
            )
        }

        launchFragmentInContainer<EditDialogFragment>(
            bundleOf(
                KEY_REGISTER_TYPE to REGISTER_TASK_EDIT,
                KEY_TASK_ID to task.id
            ),
            R.style.AppTheme
        ).also { dataBindingIdlingResource.monitorFragment(it) }

        onView(withId(R.id.edit_title)).check(
            matches(withText(R.string.edit_dialog_title_task_edit))
        )
        onView(withId(R.id.edit_input)).check(matches(withText(task.name)))
    }
}
