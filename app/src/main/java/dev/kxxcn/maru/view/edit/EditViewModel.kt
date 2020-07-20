package dev.kxxcn.maru.view.edit

import androidx.lifecycle.*
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.util.ConvertUtils
import dev.kxxcn.maru.view.home.HomeAdapter
import dev.kxxcn.maru.view.register.RegisterFilterType
import javax.inject.Inject

class EditViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _editEvent = MutableLiveData<Event<RegisterFilterType>>()
    val editEvent: LiveData<Event<RegisterFilterType>> = _editEvent

    private val _forceUpdate = MutableLiveData<Unit>()

    private val items: LiveData<List<HomeAdapter.SummaryItem>> =
        _forceUpdate.switchMap { _ ->
            repository.observeSummary().switchMap {
                MutableLiveData<List<HomeAdapter.SummaryItem>>().apply {
                    value = HomeAdapter.makeItems(it[0])
                }
            }
        }

    val name = items.map {
        it.firstOrNull()?.content?.user?.name
    }

    val wedding = items.map {
        val wedding = it.firstOrNull()?.content?.user?.wedding
        ConvertUtils.dateFormat(wedding)
    }

    val budget = items.map {
        val budget = it.firstOrNull()?.content?.user?.budget
        ConvertUtils.moneyFormat(budget)
    }

    init {
        start()
    }

    private fun start() {
        _forceUpdate.value = Unit
    }

    fun close() {
        _closeEvent.value = Event(Unit)
    }

    fun edit(filterType: RegisterFilterType) {
        _editEvent.value = Event(filterType)
    }
}
