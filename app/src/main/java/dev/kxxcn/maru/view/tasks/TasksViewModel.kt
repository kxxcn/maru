package dev.kxxcn.maru.view.tasks

import androidx.lifecycle.*
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.data.Summary
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.tasks.TasksFilterType.*
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _forceUpdate = MutableLiveData<Unit>()

    val items: LiveData<List<Summary>> =
        _forceUpdate.switchMap {
            repository.observeSummary().switchMap {
                MutableLiveData<List<Summary>>().apply {
                    value = it
                }
            }
        }

    private val _filterType = MutableLiveData<TasksFilterType>()
    val filterType: LiveData<TasksFilterType> = _filterType

    val tasks: LiveData<List<TasksAdapter.TasksItem>> =
        MediatorLiveData<List<TasksAdapter.TasksItem>>().apply {
            addSource(items) { value = tasks(it.first().tasks, filterType.value) }
            addSource(filterType) { value = tasks(items.value?.first()?.tasks, it) }
        }

    private val _taskSelectionEvent = MutableLiveData<Event<TaskDetail>>()
    val taskSelectionEvent: LiveData<Event<TaskDetail>> = _taskSelectionEvent

    private val _navigateEvent = MutableLiveData<Event<Unit>>()
    val navigateEvent: LiveData<Event<Unit>> = _navigateEvent

    private val _editEvent = MutableLiveData<Event<Unit>>()
    val editEvent: LiveData<Event<Unit>> = _editEvent

    init {
        setFiltering(values()[PreferenceUtils.taskFilterType])
        start()
    }

    private fun start() {
        _forceUpdate.value = Unit
    }

    private fun tasks(
        list: List<TaskDetail>?,
        filterType: TasksFilterType?
    ): List<TasksAdapter.TasksItem> {
        val filterItems = list
            ?.sortedBy { it.task?.priority }
            ?.filter { detail ->
                when (filterType) {
                    ALL_TASKS -> true
                    ACTIVE_TASKS -> detail.task?.isCompleted == false
                    COMPLETED_TASKS -> detail.task?.isCompleted == true
                    null -> false
                }
            } ?: emptyList()

        return TasksAdapter.makeItems(filterItems, filterType)
    }

    fun setFiltering(requestType: TasksFilterType) {
        showNavigator()
        PreferenceUtils.taskFilterType = requestType.ordinal
        _filterType.value = requestType
    }

    fun handleTargetSelection(taskDetail: TaskDetail) {
        _taskSelectionEvent.value = Event(taskDetail)
    }

    fun showNavigator() {
        _navigateEvent.value = Event(Unit)
    }

    fun editList() {
        _editEvent.value = Event(Unit)
    }
}
