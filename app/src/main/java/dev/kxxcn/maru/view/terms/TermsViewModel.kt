package dev.kxxcn.maru.view.terms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.util.KEY_TERMS_TITLE
import dev.kxxcn.maru.util.KEY_TERMS_TYPE

class TermsViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _titleRes = MutableLiveData<Int>()
    val titleRes: LiveData<Int> = _titleRes

    private val _contentRes = MutableLiveData<Int>()
    val contentRes: LiveData<Int> = _contentRes

    init {
        _contentRes.value = savedStateHandle.get<Int>(KEY_TERMS_TYPE)
        _titleRes.value = savedStateHandle.get<Int>(KEY_TERMS_TITLE)
    }

    fun close() {
        _closeEvent.value = Event(Unit)
    }
}
