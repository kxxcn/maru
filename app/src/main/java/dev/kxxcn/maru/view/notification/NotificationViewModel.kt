package dev.kxxcn.maru.view.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.util.preference.PreferenceUtils

class NotificationViewModel : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    val noticeUse = MutableLiveData<Boolean>().apply {
        value = PreferenceUtils.notifyNotice
    }

    val noticeVibrate = MutableLiveData<Boolean>().apply {
        value = PreferenceUtils.notifyNoticeVibrate
    }

    val noticeSound = MutableLiveData<Boolean>().apply {
        value = PreferenceUtils.notifyNoticeSound
    }

    fun close() {
        _closeEvent.value = Event(Unit)
    }
}
