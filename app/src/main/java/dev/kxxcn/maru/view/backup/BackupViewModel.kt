package dev.kxxcn.maru.view.backup

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Restore
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.Summary
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.succeeded
import dev.kxxcn.maru.util.DateUtils
import dev.kxxcn.maru.util.extension.decode
import dev.kxxcn.maru.util.extension.encode
import dev.kxxcn.maru.util.extension.fromJson
import dev.kxxcn.maru.util.extension.toJson
import kotlinx.coroutines.launch
import javax.inject.Inject

class BackupViewModel @Inject constructor(
    private val repository: DataRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _askEvent = MutableLiveData<Event<BackupFilterType>>()
    val askEvent: LiveData<Event<BackupFilterType>> = _askEvent

    private val _updatedTime = MutableLiveData<String>()
    val updatedTime: LiveData<String> = _updatedTime

    private val restoreData = MutableLiveData<Restore?>().apply { value = null }

    private val _progress = MutableLiveData<Boolean>().apply { value = false }
    val progress: LiveData<Boolean> = _progress

    val noData = restoreData.map { it == null }

    init {
        findRestore()
    }

    fun close() {
        _closeEvent.value = Event(Unit)
    }

    fun ask(filterType: BackupFilterType) {
        _askEvent.value = Event(filterType)
    }

    fun backup() {
        val email = auth.currentUser?.email
        if (email == null) {
            _snackbarText.value = Event(R.string.try_again_later)
        } else {
            viewModelScope.launch {
                _progress.value = true
                val result = repository.getSummary()
                if (result is Success) {
                    val encodedString = result.data.first().toJson().encode()
                    val messageRes = repository.backup(email, encodedString)
                        .takeIf { it.succeeded }
                        ?.let { R.string.success_backup }
                        ?: R.string.failure_backup

                    _snackbarText.value = Event(messageRes)
                    _progress.value = false

                    findRestore()
                }
            }
        }
    }

    fun restore() {
        val restore = restoreData.value
        if (restore == null) {
            _snackbarText.value = Event(R.string.backup_restore_no_data)
        } else {
            val decodedString = restore.data.decode()
            val summary = decodedString.fromJson<Summary>()
            viewModelScope.launch {
                val messageRes = repository.restore(summary)
                    .takeIf { it.succeeded }
                    ?.let { R.string.success_restore }
                    ?: R.string.failure_restore

                _snackbarText.value = Event(messageRes)
            }
        }
    }

    private fun findRestore() {
        val email = auth.currentUser?.email
        if (email == null) {
            _snackbarText.value = Event(R.string.try_again_later)
        } else {
            viewModelScope.launch {
                _progress.value = true
                val result = repository.findRestore(email)
                if (result is Success) {
                    val restore = result.data
                    restoreData.value = restore
                    _updatedTime.value = DateUtils.DATE_FORMAT_1.format(restore?.time)
                    _progress.value = false
                }
            }
        }
    }
}
