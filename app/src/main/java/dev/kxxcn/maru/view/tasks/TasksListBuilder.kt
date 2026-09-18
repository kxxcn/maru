package dev.kxxcn.maru.view.tasks

import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.util.AD_FIRST_AFTER
import dev.kxxcn.maru.util.AD_INTERVAL
import dev.kxxcn.maru.util.AD_MAX

/**
 * 체크리스트 목록의 상태 판정, 정렬, 필터, 광고 위치 규칙. 순수 로직이라 JVM 테스트로 고정한다.
 */
object TasksListBuilder {

    fun stateOf(detail: TaskDetail): TaskState {
        return when {
            detail.task?.isCompleted == true -> TaskState.DONE
            (detail.account?.remain ?: 0L) > 0L -> TaskState.ACTIVE
            else -> TaskState.READY
        }
    }

    fun sort(list: List<TaskDetail>): List<TaskDetail> {
        return list.sortedWith(
            compareBy<TaskDetail>({ stateOf(it).ordinal }, { it.task?.priority ?: Long.MAX_VALUE })
        )
    }

    fun filter(list: List<TaskDetail>, filterType: TasksFilterType): List<TaskDetail> {
        return when (filterType) {
            TasksFilterType.ALL_TASKS -> list
            TasksFilterType.ACTIVE_TASKS -> list.filter { stateOf(it) != TaskState.DONE }
            TasksFilterType.COMPLETED_TASKS -> list.filter { stateOf(it) == TaskState.DONE }
        }
    }

    /**
     * 광고를 넣을 위치. 값 k는 "k번째 행 뒤"를 뜻한다(1부터). 행 사이에만 넣고 목록 끝에는 넣지 않는다.
     */
    fun adPositions(rowCount: Int, isPremium: Boolean): List<Int> {
        if (isPremium) return emptyList()
        return generateSequence(AD_FIRST_AFTER) { it + AD_INTERVAL }
            .takeWhile { it < rowCount }
            .take(AD_MAX)
            .toList()
    }

    fun build(
        list: List<TaskDetail>,
        filterType: TasksFilterType,
        isPremium: Boolean
    ): List<TasksAdapter.TasksItem> {
        val rows = sort(filter(list, filterType))
        if (rows.isEmpty()) {
            val stringRes = if (filterType == TasksFilterType.COMPLETED_TASKS) {
                R.string.task_empty_completed
            } else {
                R.string.task_empty_progress
            }
            return listOf(TasksAdapter.TasksItem(TasksAdapter.TYPE_EMPTY, null, isPremium, stringRes))
        }
        val ads = adPositions(rows.size, isPremium).toSet()
        val items = mutableListOf<TasksAdapter.TasksItem>()
        rows.forEachIndexed { index, detail ->
            items.add(
                TasksAdapter.TasksItem(TasksAdapter.TYPE_ROW, detail, isPremium, state = stateOf(detail))
            )
            if ((index + 1) in ads) {
                items.add(TasksAdapter.TasksItem(TasksAdapter.TYPE_AD, null, isPremium))
            }
        }
        return items
    }
}
