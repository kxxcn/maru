package dev.kxxcn.maru.view.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObjects
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.data.Notice
import dev.kxxcn.maru.data.Result
import dev.kxxcn.maru.data.source.DataRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoticeViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _items = MutableLiveData<List<Notice>>()
    val items: LiveData<List<Notice>> = _items

    var callback: ListenerRegistration? = null

    init {
        observeNotices()
    }

    override fun onCleared() {
        callback?.remove()
        callback = null
        super.onCleared()
    }

    fun close() {
        _closeEvent.value = Event(Unit)
    }

    private fun observeNotices() {
        viewModelScope.launch {
            val result = repository.getNotices()
            if (result is Result.Success) {
                callback = result.data?.query?.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        _items.value = emptyList()
                        return@addSnapshotListener
                    }
                    _items.value = snapshot?.toObjects()
                }
            }
        }
    }
}
