package dev.kxxcn.maru.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.kxxcn.maru.Event

abstract class BaseViewModel : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _snackbarRes = MutableLiveData<Event<Int>>()
    val snackbarRes: LiveData<Event<Int>> = _snackbarRes

    private val _snackbarText = MutableLiveData<Event<String?>>()
    val snackbarText: LiveData<Event<String?>> = _snackbarText

    fun close() {
        _closeEvent.value = Event(Unit)
    }

    fun message(messageRes: Int? = null, messageText: String? = null) {
        messageRes?.let { _snackbarRes.value = Event(it) }
        messageText?.let { _snackbarText.value = Event(it) }
    }
}
