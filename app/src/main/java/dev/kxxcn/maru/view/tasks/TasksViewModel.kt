package dev.kxxcn.maru.view.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.data.Summary
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.util.COMPLETED_TASK
import dev.kxxcn.maru.util.UNCOMPLETED_TASK
import dev.kxxcn.maru.util.preference.PreferenceUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _forceUpdate = MutableLiveData<Unit>()

    val items: LiveData<List<Summary>> = _forceUpdate.switchMap {
        repository.observeSummary().switchMap { liveData { emit(it) } }
    }

    val summary: LiveData<Summary?> = items.map { it.firstOrNull() }

    val totalCount: LiveData<Int> = summary.map { it?.tasks?.size ?: 0 }

    val completedCount: LiveData<Int> = summary.map { s ->
        s?.tasks?.count { TasksListBuilder.stateOf(it) == TaskState.DONE } ?: 0
    }

    val activeCount: LiveData<Int> = summary.map { s ->
        s?.tasks?.count { TasksListBuilder.stateOf(it) != TaskState.DONE } ?: 0
    }

    val progressPercent: LiveData<Int> = summary.map { s ->
        val tasks = s?.tasks ?: emptyList()
        if (tasks.isEmpty()) {
            0
        } else {
            tasks.count { TasksListBuilder.stateOf(it) == TaskState.DONE } * 100 / tasks.size
        }
    }

    private val _filterType = MutableLiveData<TasksFilterType>()
    val filterType: LiveData<TasksFilterType> = _filterType

    val tasks: LiveData<List<TasksAdapter.TasksItem>> =
        MediatorLiveData<List<TasksAdapter.TasksItem>>().apply {
            addSource(items) {
                val summary = it.firstOrNull()
                value = tasks(
                    summary?.tasks,
                    filterType.value,
                    summary?.user?.premium ?: false
                )
            }
            addSource(filterType) {
                // 첫 진입 시 목록 데이터가 오기 전에는 빈 상태를 그리지 않는다.
                val summary = items.value?.firstOrNull() ?: return@addSource
                value = tasks(
                    summary.tasks,
                    it,
                    summary.user?.premium ?: false
                )
            }
        }

    private val _taskSelectionEvent = MutableLiveData<Event<Pair<TaskDetail, Boolean>>>()
    val taskSelectionEvent: LiveData<Event<Pair<TaskDetail, Boolean>>> = _taskSelectionEvent

    private val _editEvent = MutableLiveData<Event<Unit>>()
    val editEvent: LiveData<Event<Unit>> = _editEvent

    private val _addEvent = MutableLiveData<Event<Unit>>()
    val addEvent: LiveData<Event<Unit>> = _addEvent

    init {
        setFiltering(TasksFilterType.values()[PreferenceUtils.taskFilterType])
        start()
    }

    private fun start() {
        _forceUpdate.value = Unit
    }

    private fun tasks(
        list: List<TaskDetail>?,
        filterType: TasksFilterType?,
        isPremium: Boolean = false
    ): List<TasksAdapter.TasksItem> {
        return TasksAdapter.makeItems(list ?: emptyList(), filterType, isPremium)
    }

    fun setFiltering(requestType: TasksFilterType) {
        PreferenceUtils.forceScroll = true
        PreferenceUtils.taskFilterType = requestType.ordinal
        _filterType.value = requestType
    }

    fun select(taskDetail: TaskDetail, isPremium: Boolean) {
        PreferenceUtils.forceScroll = false
        _taskSelectionEvent.value = Event(taskDetail to isPremium)
    }

    /**
     * 체크 원 탭. 완료면 해제, 아니면 완료로 바꾼다. 지출 기록은 건드리지 않는다.
     */
    fun toggleComplete(taskDetail: TaskDetail) {
        val id = taskDetail.task?.id ?: return
        val next = if (taskDetail.task?.isCompleted == true) UNCOMPLETED_TASK else COMPLETED_TASK
        PreferenceUtils.forceScroll = false
        viewModelScope.launch {
            repository.updateTask(id, next)
        }
    }

    fun edit() {
        PreferenceUtils.forceScroll = false
        _editEvent.value = Event(Unit)
    }

    fun add() {
        PreferenceUtils.forceScroll = false
        _addEvent.value = Event(Unit)
    }

}
