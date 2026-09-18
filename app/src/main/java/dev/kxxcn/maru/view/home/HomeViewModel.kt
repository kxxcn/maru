package dev.kxxcn.maru.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.google.firebase.auth.FirebaseAuth
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.view.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: DataRepository,
    @Suppress("unused") private val auth: FirebaseAuth
) : BaseViewModel() {

    private val _forceUpdate = MutableLiveData<Unit>()

    private val _daysEvent = MutableLiveData<Event<Unit>>()
    val daysEvent: LiveData<Event<Unit>> = _daysEvent

    private val _noticeEvent = MutableLiveData<Event<Unit>>()
    val noticeEvent: LiveData<Event<Unit>> = _noticeEvent

    private val _tasksEvent = MutableLiveData<Event<Unit>>()
    val tasksEvent: LiveData<Event<Unit>> = _tasksEvent

    val items: LiveData<List<HomeAdapter.SummaryItem>> = _forceUpdate.switchMap { _ ->
        repository.observeSummary().switchMap { summaries ->
            liveData {
                summaries.firstOrNull()?.let { emit(HomeAdapter.makeItems(it)) }
            }
        }
    }

    init {
        start()
    }

    private fun start() {
        _forceUpdate.value = Unit
    }

    fun days() {
        _daysEvent.value = Event(Unit)
    }

    fun notice() {
        _noticeEvent.value = Event(Unit)
    }

    fun tasks() {
        _tasksEvent.value = Event(Unit)
    }
}
