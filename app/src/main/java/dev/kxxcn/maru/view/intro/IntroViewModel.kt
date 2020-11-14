package dev.kxxcn.maru.view.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.view.base.BaseViewModel
import javax.inject.Inject

class IntroViewModel @Inject constructor(
    repository: DataRepository
) : BaseViewModel() {

    private val _forceUpdate = MutableLiveData<Unit>()

    val hasProfile = repository.observeSummary().switchMap { liveData { emit(it.isNotEmpty()) } }

    private val _startEvent = MutableLiveData<Event<Unit>>()
    val startEvent: LiveData<Event<Unit>> = _startEvent

    private val _signInEvent = MutableLiveData<Event<Unit>>()
    val signInEvent: LiveData<Event<Unit>> = _signInEvent

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
}
