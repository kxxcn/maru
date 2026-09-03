package dev.kxxcn.maru.view.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.succeeded
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.view.base.BaseViewModel
import javax.inject.Inject

class SettingViewModel @Inject constructor(
    private val repository: DataRepository,
    private val auth: FirebaseAuth
) : BaseViewModel() {

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

    private val _deleteAccount = MutableLiveData<Event<Unit>>()
    val deleteAccount: LiveData<Event<Unit>> = _deleteAccount

    private val _accountDeleted = MutableLiveData<Event<Unit>>()
    val accountDeleted: LiveData<Event<Unit>> = _accountDeleted

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

    fun deleteAccount() {
        _deleteAccount.value = Event(Unit)
    }

    fun confirmDeleteAccount() {
        val email = auth.currentUser?.email
        if (email == null) {
            message(R.string.try_again_later)
            return
        }
        viewModelScope.launch {
            val result = repository.deleteAccountData(email)
            if (result.succeeded) {
                deleteFirebaseAccount()
                message(R.string.setting_delete_account_succeeded)
                _accountDeleted.value = Event(Unit)
            } else {
                message(R.string.setting_delete_account_failed)
            }
        }
    }

    private suspend fun deleteFirebaseAccount() {
        try {
            auth.currentUser?.delete()?.await()
        } catch (e: Exception) {
            auth.signOut()
            message(R.string.setting_delete_account_sign_in_required)
        }
    }
}
