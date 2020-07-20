package dev.kxxcn.maru.view.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R

class TimelineViewModel : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    val remainRes: Int = R.string.home_welcome_card_remain

    val items = TimelineItem.create()

    fun close() {
        _closeEvent.value = Event(Unit)
    }
}
