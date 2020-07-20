package dev.kxxcn.maru.view.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.source.DataRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class StatusViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _motionEvent = MutableLiveData<Event<Unit>>()
    val motionEvent: LiveData<Event<Unit>> = _motionEvent

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _totalTasksCount = MutableLiveData<Int>()
    val totalTasksCount: LiveData<Int> = _totalTasksCount

    private val _progressTasksCount = MutableLiveData<Int>()
    val progressTasksCount: LiveData<Int> = _progressTasksCount

    private val _completedTasksCount = MutableLiveData<Int>()
    val completedTasksCount: LiveData<Int> = _completedTasksCount

    private val _tasksProgress = MutableLiveData<Double>()
    val tasksProgress: LiveData<Double> = _tasksProgress

    init {
        start()
    }

    private fun start() {
        viewModelScope.launch {
            val result = repository.getTasks()
            if (result is Success) {
                val tasks = result.data
                val totalCount = tasks.size
                val completedCount = tasks.filter { it.isCompleted }.size
                _totalTasksCount.value = totalCount
                _progressTasksCount.value = tasks.filter { !it.isCompleted }.size
                _completedTasksCount.value = completedCount
                _tasksProgress.value = (completedCount.toDouble() / totalCount) * 100
                _motionEvent.value = Event(Unit)
            }
        }
    }

    fun close() {
        _closeEvent.value = Event(Unit)
    }
}
