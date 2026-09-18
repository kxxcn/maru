package dev.kxxcn.maru.view.tasks

import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Account
import dev.kxxcn.maru.data.Task
import dev.kxxcn.maru.data.TaskDetail
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TasksListBuilderTest {

    private fun detail(
        name: String,
        priority: Long,
        completed: Boolean = false,
        remain: Long? = null
    ): TaskDetail = TaskDetail().apply {
        val t = Task(name = name, priority = priority, isCompleted = completed)
        task = t
        account = remain?.let { Account(husband = 100L, wife = 0L, remain = it, taskId = t.id) }
    }

    @Test
    fun `completed task is DONE even if remain is positive`() {
        assertEquals(TaskState.DONE, TasksListBuilder.stateOf(detail("a", 0, completed = true, remain = 500)))
    }

    @Test
    fun `task with zero remain is READY even if account exists`() {
        assertEquals(TaskState.READY, TasksListBuilder.stateOf(detail("a", 0, remain = 0)))
        assertEquals(TaskState.ACTIVE, TasksListBuilder.stateOf(detail("a", 0, remain = 100)))
    }

    @Test
    fun `task without account is READY`() {
        assertEquals(TaskState.READY, TasksListBuilder.stateOf(detail("a", 0)))
    }

    @Test
    fun `sort groups active then ready then done and keeps priority inside a group`() {
        val list = listOf(
            detail("done1", 0, completed = true),
            detail("ready2", 1),
            detail("active3", 2, remain = 10),
            detail("ready4", 3),
            detail("active5", 4, remain = 0),
            detail("done6", 5, completed = true)
        )
        val names = TasksListBuilder.sort(list).map { it.task?.name }
        assertEquals(listOf("active3", "ready2", "ready4", "active5", "done1", "done6"), names)
    }

    @Test
    fun `ad positions are after 4th and 14th row at most two and only between rows`() {
        assertEquals(listOf(4, 14), TasksListBuilder.adPositions(18, false))
        assertEquals(listOf(4), TasksListBuilder.adPositions(10, false))
        assertEquals(listOf(4), TasksListBuilder.adPositions(14, false))
        assertEquals(emptyList<Int>(), TasksListBuilder.adPositions(4, false))
        assertEquals(emptyList<Int>(), TasksListBuilder.adPositions(18, true))
    }

    @Test
    fun `build inserts ads at index 4 and 15 for 18 rows`() {
        val list = (0 until 18).map { detail("t$it", it.toLong()) }
        val items = TasksListBuilder.build(list, TasksFilterType.ALL_TASKS, isPremium = false)
        assertEquals(20, items.size)
        assertEquals(TasksAdapter.TYPE_AD, items[4].viewType)
        assertEquals(TasksAdapter.TYPE_AD, items[15].viewType)
        assertEquals(2, items.count { it.viewType == TasksAdapter.TYPE_AD })
        assertEquals("t3", items[3].taskDetail?.task?.name)
        assertEquals("t4", items[5].taskDetail?.task?.name)
    }

    @Test
    fun `build has no ads for premium`() {
        val list = (0 until 18).map { detail("t$it", it.toLong()) }
        val items = TasksListBuilder.build(list, TasksFilterType.ALL_TASKS, isPremium = true)
        assertEquals(18, items.size)
        assertEquals(0, items.count { it.viewType == TasksAdapter.TYPE_AD })
    }

    @Test
    fun `build carries state on each row`() {
        val list = listOf(detail("a", 0, remain = 10), detail("b", 1), detail("c", 2, completed = true))
        val items = TasksListBuilder.build(list, TasksFilterType.ALL_TASKS, isPremium = true)
        assertEquals(listOf(TaskState.ACTIVE, TaskState.READY, TaskState.DONE), items.map { it.state })
    }

    @Test
    fun `active filter hides done rows and completed filter shows only done`() {
        val list = listOf(detail("a", 0, remain = 10), detail("b", 1), detail("c", 2, completed = true))
        assertEquals(2, TasksListBuilder.build(list, TasksFilterType.ACTIVE_TASKS, true).size)
        assertEquals(1, TasksListBuilder.build(list, TasksFilterType.COMPLETED_TASKS, true).size)
    }

    @Test
    fun `empty list returns one empty item with filter specific text`() {
        val completed = TasksListBuilder.build(emptyList(), TasksFilterType.COMPLETED_TASKS, false)
        val progress = TasksListBuilder.build(emptyList(), TasksFilterType.ACTIVE_TASKS, false)
        assertEquals(1, completed.size)
        assertEquals(TasksAdapter.TYPE_EMPTY, completed[0].viewType)
        assertEquals(R.string.task_empty_completed, completed[0].stringRes)
        assertEquals(R.string.task_empty_progress, progress[0].stringRes)
        assertNull(completed[0].taskDetail)
    }
}
