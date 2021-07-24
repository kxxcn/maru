package dev.kxxcn.maru.view.intro

import androidx.lifecycle.*
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Result
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.view.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class IntroViewModel @Inject constructor(
    private val repository: DataRepository
) : BaseViewModel() {

    private val _forceUpdate = MutableLiveData<Unit>()

    val hasProfile = repository.observeSummary().switchMap { liveData { emit(it.isNotEmpty()) } }

    private val _startEvent = MutableLiveData<Event<Unit>>()
    val startEvent: LiveData<Event<Unit>> = _startEvent

    private val _signInEvent = MutableLiveData<Event<Unit>>()
    val signInEvent: LiveData<Event<Unit>> = _signInEvent

    private val _backupEvent = MutableLiveData<Event<Unit>>()
    val backupEvent: LiveData<Event<Unit>> = _backupEvent

    init {
        _forceUpdate.value = Unit
    }

    fun start() {
        _startEvent.value = Event(Unit)
    }

    fun restore() {
        _signInEvent.value = Event(Unit)
    }

    fun handleSignInFailure() {
        message(R.string.failure_sign_in)
    }

    fun isPremium(email: String?) {
        viewModelScope.launch {
            val result = repository.isPremium(email)
            if (result is Result.Success && result.data) {
                backup()
            } else {
                message(R.string.use_after_registering_as_a_member)
            }
        }
    }

    private fun backup() {
        _backupEvent.value = Event(Unit)
    }
}
