package dev.kxxcn.maru.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import dev.kxxcn.maru.data.source.DataRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _forceUpdate = MutableLiveData<Unit>()

    val items: LiveData<List<HomeAdapter.SummaryItem>> =
        _forceUpdate.switchMap { _ ->
            repository.observeSummary().switchMap {
                MutableLiveData<List<HomeAdapter.SummaryItem>>().apply {
                    value = HomeAdapter.makeItems(it[0])
                }
            }
        }

    init {
        start()
    }

    private fun start() {
        _forceUpdate.value = Unit
    }
}
