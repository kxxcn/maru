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
import kotlinx.coroutines.launch

class SortViewModel @AssistedInject constructor(
    private val repository: DataRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<SortViewModel>

    private val _forceUpdate = MutableLiveData<Unit>()

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _updateEvent = MutableLiveData<Event<Unit>>()
    val updateEvent: LiveData<Event<Unit>> = _updateEvent

    private val _deleteEvent = MutableLiveData<Event<Int>>()
    val deleteEvent: LiveData<Event<Int>> = _deleteEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>> = _toastText

    val items: LiveData<List<Summary>> =
        _forceUpdate.switchMap {
            repository.observeSummary().switchMap {
                MutableLiveData<List<Summary>>().apply {
                    value = it
                }
            }
        }

    val tasks: LiveData<List<Task?>> = items.map {
        it.flatMap { summary -> summary.tasks }
            .map { detail -> detail.task }
            .sortedBy { task -> task?.priority }
    }

    private val _basicColorRes = MutableLiveData<Int>()
    val basicColorRes: LiveData<Int> = _basicColorRes

    private val _deletableColorRes = MutableLiveData<Int>()
    val deletableColorRes: LiveData<Int> = _deletableColorRes

    private val _deleteIconActiveColorRes = MutableLiveData<Int>()
    val deleteIconActiveColorRes: LiveData<Int> = _deleteIconActiveColorRes

    private val _deleteIconInactiveColorRes = MutableLiveData<Int>()
    val deleteIconInactiveColorRes: LiveData<Int> = _deleteIconInactiveColorRes

    val tasksJson: LiveData<String> = getSavedDeletableSetLiveData()

    val isEmpty: LiveData<Boolean> = getSavedDeletableSetLiveData().map {
        Gson().fromJson(it, Array<Task>::class.java).toList().isEmpty()
    }

    init {
        _basicColorRes.value =
            if (PreferenceUtils.useDarkMode) R.color.colorPrimaryDarkNight else R.color.editBasicBackground
        _deletableColorRes.value =
            if (PreferenceUtils.useDarkMode) R.color.editDeletableBackgroundNight else R.color.editDeletableBackground
        _deleteIconActiveColorRes.value = R.color.editDeleteIconActiveTint
        _deleteIconInactiveColorRes.value = R.color.editDeleteIconInActiveTint
        start()
    }

    private fun start() {
        _forceUpdate.value = Unit
        val set = convertSavedDeletableSet()
        savedStateHandle.set(EDIT_DELETABLE_SET_SAVED_STATE_KEY, Gson().toJson(set))
    }

    private fun getSavedDeletableSet(): String? {
        return savedStateHandle.get(EDIT_DELETABLE_SET_SAVED_STATE_KEY)
    }

    private fun getSavedDeletableSetLiveData(): LiveData<String> {
        return savedStateHandle.getLiveData(EDIT_DELETABLE_SET_SAVED_STATE_KEY)
    }

    private fun clearSet() {
        savedStateHandle.set(
            EDIT_DELETABLE_SET_SAVED_STATE_KEY,
            Gson().toJson(emptySet<Task>())
        )
    }

    private fun close() {
        _closeEvent.value = Event(Unit)
    }

    /**
     * "SavedState"의 Json 문자열을 "Set"으로 변환
     */
    fun convertSavedDeletableSet(): MutableSet<Task> {
        return getSavedDeletableSet()
            ?.let { Gson().fromJson(it, Array<Task>::class.java).toMutableSet() }
            ?: mutableSetOf()
    }

    fun onClickTask(task: Task) {
        convertSavedDeletableSet().apply {
            if (contains(task)) {
                remove(task)
            } else {
                add(task)
            }
        }.also { set ->
            savedStateHandle.set(EDIT_DELETABLE_SET_SAVED_STATE_KEY, Gson().toJson(set))
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
                    if (origin != name) _toastText.value = Event(R.string.sort_tasks_update)
                    close()
                }
            }
        }
    }

    fun handleDeletionClick() {
        val tasksSet = convertSavedDeletableSet()
        if (tasksSet.isEmpty()) return
        _deleteEvent.value = Event(tasksSet.size)
    }

    fun deleteTasks() {
        viewModelScope.launch {
            val result = repository.deleteTasks(convertSavedDeletableSet().toList())
            if (result.succeeded) {
                clearSet()
                _snackbarText.value = Event(R.string.sort_tasks_delete)
            }
        }
    }
}
