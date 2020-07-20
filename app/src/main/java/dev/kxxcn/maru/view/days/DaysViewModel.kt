package dev.kxxcn.maru.view.days

import androidx.lifecycle.*
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Day
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.succeeded
import dev.kxxcn.maru.view.home.HomeAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class DaysViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _forceUpdate = MutableLiveData<Unit>()

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _addEvent = MutableLiveData<Event<Unit>>()
    val addEvent: LiveData<Event<Unit>> = _addEvent

    private val _deleteEvent = MutableLiveData<Event<Day>>()
    val deleteEvent: LiveData<Event<Day>> = _deleteEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val items: LiveData<List<HomeAdapter.SummaryItem>> =
        _forceUpdate.switchMap { _ ->
            repository.observeSummary().switchMap {
                MutableLiveData<List<HomeAdapter.SummaryItem>>().apply {
                    value = HomeAdapter.makeItems(it[0])
                }
            }
        }

    val days = items.map {
        it.firstOrNull()?.content?.days?.reversed()
    }

    val isEmpty = items.map {
        it.firstOrNull()?.content?.days?.isNullOrEmpty() ?: false
    }

    init {
        start()
    }

    fun start() {
        _forceUpdate.value = Unit
    }

    fun close() {
        _closeEvent.value = Event(Unit)
    }

    fun add() {
        _addEvent.value = Event(Unit)
    }

    fun delete(position: Int) {
        val day = days.value?.get(position) ?: return
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
            _snackbarText.value = Event(messageRes)
        }
    }
}
