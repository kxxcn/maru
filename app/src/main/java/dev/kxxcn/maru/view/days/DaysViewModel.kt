package dev.kxxcn.maru.view.days

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Day
import dev.kxxcn.maru.data.Summary
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.succeeded
import dev.kxxcn.maru.view.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class DaysViewModel @Inject constructor(
    private val repository: DataRepository
) : BaseViewModel() {

    private val _forceUpdate = MutableLiveData<Unit>()

    private val _addEvent = MutableLiveData<Event<Unit>>()
    val addEvent: LiveData<Event<Unit>> = _addEvent

    private val _deleteEvent = MutableLiveData<Event<Day>>()
    val deleteEvent: LiveData<Event<Day>> = _deleteEvent

    val summary: LiveData<Summary?> = _forceUpdate.switchMap {
        repository.observeSummary().map { it.firstOrNull() }
    }

    /** 다가오는 날부터. */
    val days: LiveData<List<Day>> = summary.map { it?.days?.sortedBy { day -> day.date } ?: emptyList() }

    val isEmpty: LiveData<Boolean> = summary.map { it?.days.isNullOrEmpty() }

    init {
        start()
    }

    fun start() {
        _forceUpdate.value = Unit
    }

    fun add() {
        _addEvent.value = Event(Unit)
    }

    fun delete(position: Int) {
        val day = days.value?.getOrNull(position) ?: return
        _deleteEvent.value = Event(day)
    }

    fun handleDeletionSelection(day: Day) {
        viewModelScope.launch {
            val result = repository.deleteDay(day)
            val messageRes = if (result.succeeded) {
                R.string.days_deletion_succeeded
            } else {
                R.string.days_deletion_failed
            }
            message(messageRes)
        }
    }
}
