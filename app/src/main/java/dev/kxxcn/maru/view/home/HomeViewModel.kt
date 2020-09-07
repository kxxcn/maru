package dev.kxxcn.maru.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.google.firebase.auth.FirebaseAuth
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.view.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: DataRepository,
    private val auth: FirebaseAuth
) : BaseViewModel() {

    private val _forceUpdate = MutableLiveData<Unit>()

    private val _daysEvent = MutableLiveData<Event<Unit>>()
    val daysEvent: LiveData<Event<Unit>> = _daysEvent

    val items: LiveData<List<HomeAdapter.SummaryItem>> =
        _forceUpdate.switchMap { _ ->
            repository.observeSummary().switchMap {
                MutableLiveData<List<HomeAdapter.SummaryItem>>().apply {
                    value = HomeAdapter.makeItems(it[0])
                }
            }
        }

    val verified = MutableLiveData<Boolean>().apply { value = auth.currentUser != null }

    init {
        start()
    }

    private fun start() {
        _forceUpdate.value = Unit
    }

    fun description() {
        message(R.string.home_welcome_card_verified)
    }

    fun days() {
        _daysEvent.value = Event(Unit)
    }
}
