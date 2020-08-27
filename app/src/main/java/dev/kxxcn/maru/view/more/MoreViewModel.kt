package dev.kxxcn.maru.view.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.view.more.contents.ContentsItem
import dev.kxxcn.maru.view.more.contents.ContentsItem.*
import dev.kxxcn.maru.view.more.contents.MoreContentsAdapter
import dev.kxxcn.maru.view.present.PresentFilterType
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoreViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _settingEvent = MutableLiveData<Event<Unit>>()
    val settingEvent: LiveData<Event<Unit>> = _settingEvent

    private val _contactEvent = MutableLiveData<Event<Unit>>()
    val contactEvent: LiveData<Event<Unit>> = _contactEvent

    private val _noticeEvent = MutableLiveData<Event<Unit>>()
    val noticeEvent: LiveData<Event<Unit>> = _noticeEvent

    private val _storeEvent = MutableLiveData<Event<Unit>>()
    val storeEvent: LiveData<Event<Unit>> = _storeEvent

    private val _orderEvent = MutableLiveData<Event<Unit>>()
    val orderEvent: LiveData<Event<Unit>> = _orderEvent

    private val _adEvent = MutableLiveData<Event<Unit>>()
    val adEvent: LiveData<Event<Unit>> = _adEvent

    private val _backupEvent = MutableLiveData<Event<Unit>>()
    val backupEvent: LiveData<Event<Unit>> = _backupEvent

    private val _nightEvent = MutableLiveData<Event<Unit>>()
    val nightEvent: LiveData<Event<Unit>> = _nightEvent

    private val _daysEvent = MutableLiveData<Event<Unit>>()
    val daysEvent: LiveData<Event<Unit>> = _daysEvent

    private val _timelineEvent = MutableLiveData<Event<Unit>>()
    val timelineEvent: LiveData<Event<Unit>> = _timelineEvent

    private val _presentEvent = MutableLiveData<Event<PresentFilterType>>()
    val presentEvent: LiveData<Event<PresentFilterType>> = _presentEvent

    private val _landmarkEvent = MutableLiveData<Event<Unit>>()
    val landmarkEvent: LiveData<Event<Unit>> = _landmarkEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _purchaseEvent = MutableLiveData<Event<Unit>>()
    val purchaseEvent: LiveData<Event<Unit>> = _purchaseEvent

    private val _premiumEvent = MutableLiveData<Event<Unit>>()
    val premiumEvent: LiveData<Event<Unit>> = _premiumEvent

    val moreItems: List<MoreAdapter.MoreItem> = MoreAdapter.makeItems()

    val contentsItems: List<ContentsItem> = MoreContentsAdapter.makeItems()

    fun settings() {
        _settingEvent.value = Event(Unit)
    }

    fun contact() {
        _contactEvent.value = Event(Unit)
    }

    fun notice() {
        _noticeEvent.value = Event(Unit)
    }

    private fun review() {
        _storeEvent.value = Event(Unit)
    }

    private fun order() {
        _orderEvent.value = Event(Unit)
    }

    private fun ad() {
        _adEvent.value = Event(Unit)
    }

    private fun backup() {
        _backupEvent.value = Event(Unit)
    }

    private fun night() {
        _nightEvent.value = Event(Unit)
    }

    private fun days() {
        _daysEvent.value = Event(Unit)
    }

    private fun timeline() {
        _timelineEvent.value = Event(Unit)
    }

    private fun present(filterType: PresentFilterType) {
        _presentEvent.value = Event(filterType)
    }

    private fun landmark() {
        _landmarkEvent.value = Event(Unit)
    }

    fun contents(filterType: ContentsItem) {
        when (filterType) {
            AD -> ad()
            BACKUP -> backup()
            NIGHT -> night()
            REVIEW -> review()
            DAYS -> days()
            TIMELINE -> timeline()
            ORDER -> order()
            RING -> present(PresentFilterType.RING)
            DRESS -> present(PresentFilterType.DRESS)
            TUXEDO -> present(PresentFilterType.TUXEDO)
            HANBOK -> present(PresentFilterType.HANBOK)
            LANDMARK -> landmark()
        }
    }

    fun handleSignInSuccess() {
        _snackbarText.value = Event(R.string.success_sign_in)
    }

    fun handleSignInFailure() {
        _snackbarText.value = Event(R.string.failure_sign_in)
    }

    fun isPremium(email: String?, filterType: ContentsItem) {
        viewModelScope.launch {
            val result = repository.isPremium(email)
            if (result is Success && result.data) {
                when (filterType) {
                    AD -> _snackbarText.value = Event(R.string.apply_ad_removal_function)
                    BACKUP -> _premiumEvent.value = Event(Unit)
                }
            } else {
                _purchaseEvent.value = Event(Unit)
            }
        }
    }

    fun handleReviewSuccess() {
        _snackbarText.value = Event(R.string.thank_you_for_feedback)
    }
}
