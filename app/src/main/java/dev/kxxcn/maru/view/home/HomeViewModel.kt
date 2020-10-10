package dev.kxxcn.maru.view.home

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.view.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: DataRepository,
    private val auth: FirebaseAuth
) : BaseViewModel() {

    private val _forceUpdate = MutableLiveData<Unit>()

    private val _daysEvent = MutableLiveData<Event<Unit>>()
    val daysEvent: LiveData<Event<Unit>> = _daysEvent

    private val _shareEvent = MutableLiveData<Event<Unit>>()
    val shareEvent: LiveData<Event<Unit>> = _shareEvent

    val items: LiveData<List<HomeAdapter.SummaryItem>> = _forceUpdate.switchMap { _ ->
        repository.observeSummary().switchMap { liveData { emit(HomeAdapter.makeItems(it[0])) } }
    }

    private val _isLoading = MutableLiveData<Boolean>().apply { value = false }
    val isLoading: LiveData<Boolean> = _isLoading

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

    fun share() {
        _shareEvent.value = Event(Unit)
    }

    fun isLoading(isLoading: Boolean) {
        viewModelScope.launch { _isLoading.value = isLoading }
    }
}
