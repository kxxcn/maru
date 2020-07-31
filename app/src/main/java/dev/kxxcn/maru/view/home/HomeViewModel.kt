package dev.kxxcn.maru.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.google.firebase.auth.FirebaseAuth
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.source.DataRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: DataRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _forceUpdate = MutableLiveData<Unit>()

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    val items: LiveData<List<HomeAdapter.SummaryItem>> =
        _forceUpdate.switchMap { _ ->
            repository.observeSummary().switchMap {
                MutableLiveData<List<HomeAdapter.SummaryItem>>().apply {
                    value = HomeAdapter.makeItems(it[0])
                }
            }
        }

    val verified = MutableLiveData<Boolean>().apply { value = auth.currentUser != null }

    init {
        start()
    }

    private fun start() {
        _forceUpdate.value = Unit
    }

    fun description() {
        _snackbarText.value = Event(R.string.home_welcome_card_verified)
    }
}
