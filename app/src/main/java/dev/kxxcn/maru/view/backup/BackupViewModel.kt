package dev.kxxcn.maru.view.backup

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Restore
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.Summary
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.succeeded
import dev.kxxcn.maru.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.maru.util.DateUtils
import dev.kxxcn.maru.util.KEY_BACKUP_TYPE
import dev.kxxcn.maru.util.extension.decode
import dev.kxxcn.maru.util.extension.encode
import dev.kxxcn.maru.util.extension.fromJson
import dev.kxxcn.maru.util.extension.toJson
import dev.kxxcn.maru.view.backup.BackupFilterType.BACKUP
import dev.kxxcn.maru.view.base.BaseViewModel
import kotlinx.coroutines.launch

class BackupViewModel @AssistedInject constructor(
    private val repository: DataRepository,
    private val auth: FirebaseAuth,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<BackupViewModel>

    val filterType = savedStateHandle
        .get<BackupFilterType>(KEY_BACKUP_TYPE)
        ?: BACKUP

    private val _askEvent = MutableLiveData<Event<BackupFilterType>>()
    val askEvent: LiveData<Event<BackupFilterType>> = _askEvent

    private val _updatedTime = MutableLiveData<String>()
    val updatedTime: LiveData<String> = _updatedTime

    private val restoreData = MutableLiveData<Restore?>().apply { value = null }

    private val _progress = MutableLiveData<Boolean>().apply { value = false }
    val progress: LiveData<Boolean> = _progress

    val noData = restoreData.map { it == null }

    val email = MutableLiveData<String?>().apply { value = auth.currentUser?.email }

    init {
        findRestore()
    }

    fun ask(filterType: BackupFilterType) {
        _askEvent.value = Event(filterType)
    }

    fun backup() {
        val email = auth.currentUser?.email
        if (email == null) {
            message(R.string.try_again_later)
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
                    findRestore()
                    messageRes
                } else {
                    R.string.try_again_later
                }.also {
                    message(it)
                }
                _progress.value = false
            }
        }
    }

    fun restore() {
        val restore = restoreData.value
        if (restore == null) {
            message(R.string.backup_restore_no_data)
        } else {
            val decodedString = restore.data.decode()
            val summary = decodedString.fromJson<Summary>()
            viewModelScope.launch {
                val messageRes = repository.restore(summary)
                    .takeIf { it.succeeded }
                    ?.let { R.string.success_restore }
                    ?: R.string.failure_restore

                message(messageRes)
            }
        }
    }

    private fun findRestore() {
        val email = auth.currentUser?.email
        if (email == null) {
            message(R.string.try_again_later)
        } else {
            viewModelScope.launch {
                _progress.value = true
                val result = repository.findRestore(email)
                if (result is Success) {
                    result.data
                        .also { restoreData.value = it }
                        ?.let { _updatedTime.value = DateUtils.DATE_FORMAT_1.format(it.time) }
                } else {
                    message(R.string.try_again_later)
                }
                _progress.value = false
            }
        }
    }
}
