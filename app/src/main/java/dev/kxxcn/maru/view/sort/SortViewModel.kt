package dev.kxxcn.maru.view.sort

import androidx.lifecycle.*
import com.google.gson.Gson
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.Summary
import dev.kxxcn.maru.data.Task
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.succeeded
import dev.kxxcn.maru.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.maru.util.EDIT_DELETABLE_SET_SAVED_STATE_KEY
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.BaseViewModel
import kotlinx.coroutines.launch

class SortViewModel @AssistedInject constructor(
    private val repository: DataRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<SortViewModel>

    private val _forceUpdate = MutableLiveData<Unit>()

    private val _deleteEvent = MutableLiveData<Event<Int>>()
    val deleteEvent: LiveData<Event<Int>> = _deleteEvent

    private val _addTaskEvent = MutableLiveData<Event<Unit>>()
    val addTaskEvent: LiveData<Event<Unit>> = _addTaskEvent

    val items: LiveData<List<Summary>> = _forceUpdate.switchMap {
        repository.observeSummary()
            .distinctUntilChanged()
            .switchMap { liveData { emit(value = it) } }
    }

    val tasks: LiveData<List<Task?>> = items.map {
        it.flatMap { summary -> summary.tasks }
            .map { detail -> detail.task }
            .sortedBy { task -> task?.priority }
    }

    private val _basicColorRes = MutableLiveData<Int>().apply {
        value =
            if (PreferenceUtils.useDarkMode) R.color.colorPrimaryDarkNight else R.color.editBasicBackground
    }
    val basicColorRes: LiveData<Int> = _basicColorRes

    private val _deletableColorRes = MutableLiveData<Int>().apply {
        value =
            if (PreferenceUtils.useDarkMode) R.color.editDeletableBackgroundNight else R.color.editDeletableBackground
    }
    val deletableColorRes: LiveData<Int> = _deletableColorRes

    private val _deleteIconActiveColorRes =
        MutableLiveData<Int>().apply { value = R.color.editDeleteIconActiveTint }
    val deleteIconActiveColorRes: LiveData<Int> = _deleteIconActiveColorRes

    private val _deleteIconInactiveColorRes =
        MutableLiveData<Int>().apply { value = R.color.editDeleteIconInActiveTint }
    val deleteIconInactiveColorRes: LiveData<Int> = _deleteIconInactiveColorRes

    private val deletableSource: LiveData<String> =
        savedStateHandle.getLiveData(EDIT_DELETABLE_SET_SAVED_STATE_KEY)

    val deletableSet: LiveData<Set<Task>> = deletableSource.map {
        Gson().fromJson(it, Array<Task>::class.java).toSet()
    }

    val isEmpty: LiveData<Boolean> = deletableSource.map {
        Gson().fromJson(it, Array<Task>::class.java).isEmpty()
    }

    init {
        start()
    }

    private fun start() {
        _forceUpdate.value = Unit
        savedStateHandle.set(EDIT_DELETABLE_SET_SAVED_STATE_KEY, Gson().toJson(convertJsonToSet()))
    }

    private fun clearSet() {
        savedStateHandle.set(
            EDIT_DELETABLE_SET_SAVED_STATE_KEY,
            Gson().toJson(emptySet<Task>())
        )
    }

    /**
     * "SavedState"의 Json 문자열을 "Set"으로 변환
     */
    private fun convertJsonToSet(): MutableSet<Task> {
        return deletableSource.value
            ?.let { Gson().fromJson(it, Array<Task>::class.java).toMutableSet() }
            ?: mutableSetOf()
    }

    fun taskSelectionToDelete(task: Task) {
        with(convertJsonToSet()) {
            if (contains(task)) {
                remove(task)
            } else {
                add(task)
            }
            savedStateHandle.set(
                EDIT_DELETABLE_SET_SAVED_STATE_KEY,
                Gson().toJson(this)
            )
        }
    }

    fun saveTasks() {
        viewModelScope.launch {
            val source = tasks.value ?: emptyList()
            val target = mutableListOf<Task>()
            for ((index, task) in source.withIndex()) {
                task ?: continue
                task.priority = index.toLong()
                target.add(task)
            }

            val fetchResult = repository.getTasks()
            if (fetchResult is Success) {
                // DB 업데이트 전 체크리스트 목록
                val origin = fetchResult.data
                    .sortedBy { it.priority }
                    .map { it.name }

                val updateResult = repository.updateTasks(target)
                if (updateResult.succeeded) {
                    // DB 업데이트 후 체크리스트 목록
                    val name = source.map { it?.name }
                    // 전후를 비교해서 변경되었을 때 토스트 메시지 표시
                    if (origin != name) toast(R.string.sort_tasks_update)
                    close()
                }
            }
        }
    }

    fun handleTasksDeletion() {
        deletableSet.value
            ?.takeUnless { it.isEmpty() }
            ?.let { _deleteEvent.value = Event(it.size) }
    }

    fun handleTasksAddition() {
        _addTaskEvent.value = Event(Unit)
    }

    fun deleteTasks() {
        viewModelScope.launch {
            val tasks = deletableSet.value
                ?.toList()
                ?.takeIf { it.isNotEmpty() }
                ?: return@launch
            val result = repository.deleteTasks(tasks)
            if (result.succeeded) {
                clearSet()
                message(R.string.sort_tasks_delete)
            }
        }
    }
}
