package dev.kxxcn.maru.view.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.kxxcn.maru.Event

class SettingViewModel : ViewModel() {

    private val _profile = MutableLiveData<Event<Unit>>()
    val profile: LiveData<Event<Unit>> = _profile

    private val _tasks = MutableLiveData<Event<Unit>>()
    val tasks: LiveData<Event<Unit>> = _tasks

    private val _notice = MutableLiveData<Event<Unit>>()
    val notice: LiveData<Event<Unit>> = _notice

    private val _location = MutableLiveData<Event<Unit>>()
    val location: LiveData<Event<Unit>> = _location

    private val _license = MutableLiveData<Event<Unit>>()
    val license: LiveData<Event<Unit>> = _license

    fun editProfile() {
        _profile.value = Event(Unit)
    }

    fun editTasks() {
        _tasks.value = Event(Unit)
    }

    fun noticeNotification() {
        _notice.value = Event(Unit)
    }

    fun termsLocationBasedService() {
        _location.value = Event(Unit)
    }

    fun termsLicense() {
        _license.value = Event(Unit)
    }
}
