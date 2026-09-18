package dev.kxxcn.maru.view.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.Summary
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.BaseViewModel
import dev.kxxcn.maru.view.present.PresentFilterType
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoreViewModel @Inject constructor(
    private val repository: DataRepository
) : BaseViewModel() {

    val summary: LiveData<Summary?> = repository.observeSummary().map { it.firstOrNull() }

    /** 광고 노출 여부의 단일 기준. Room의 User.premium. */
    val isPremium: LiveData<Boolean> = summary.map { it?.user?.premium ?: false }

    private val _useDarkMode = MutableLiveData(PreferenceUtils.useDarkMode)
    val useDarkMode: LiveData<Boolean> = _useDarkMode

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

    private val _nightEvent = MutableLiveData<Event<Boolean>>()
    val nightEvent: LiveData<Event<Boolean>> = _nightEvent

    private val _daysEvent = MutableLiveData<Event<Unit>>()
    val daysEvent: LiveData<Event<Unit>> = _daysEvent

    private val _timelineEvent = MutableLiveData<Event<Unit>>()
    val timelineEvent: LiveData<Event<Unit>> = _timelineEvent

    private val _presentEvent = MutableLiveData<Event<PresentFilterType>>()
    val presentEvent: LiveData<Event<PresentFilterType>> = _presentEvent

    private val _landmarkEvent = MutableLiveData<Event<Unit>>()
    val landmarkEvent: LiveData<Event<Unit>> = _landmarkEvent

    private val _purchaseEvent = MutableLiveData<Event<Unit>>()
    val purchaseEvent: LiveData<Event<Unit>> = _purchaseEvent

    private val _premiumEvent = MutableLiveData<Event<Unit>>()
    val premiumEvent: LiveData<Event<Unit>> = _premiumEvent

    fun settings() {
        _settingEvent.value = Event(Unit)
    }

    fun contact() {
        _contactEvent.value = Event(Unit)
    }

    fun notice() {
        _noticeEvent.value = Event(Unit)
    }

    fun review() {
        _storeEvent.value = Event(Unit)
    }

    fun order() {
        _orderEvent.value = Event(Unit)
    }

    fun premium() {
        _adEvent.value = Event(Unit)
    }

    fun backup() {
        _backupEvent.value = Event(Unit)
    }

    /** 야간모드 토글. 확인 다이얼로그 없이 바로 바꾼다. */
    fun setNight(enabled: Boolean) {
        if (_useDarkMode.value == enabled) return
        _useDarkMode.value = enabled
        _nightEvent.value = Event(enabled)
    }

    fun days() {
        _daysEvent.value = Event(Unit)
    }

    fun timeline() {
        _timelineEvent.value = Event(Unit)
    }

    fun present(filterType: PresentFilterType) {
        _presentEvent.value = Event(filterType)
    }

    fun landmark() {
        _landmarkEvent.value = Event(Unit)
    }

    fun handleSignInSuccess() {
        message(R.string.success_sign_in)
    }

    fun handleSignInFailure() {
        message(R.string.failure_sign_in)
    }

    /**
     * 프리미엄 여부를 확인해 백업 화면 또는 구매 화면으로 보낸다.
     * 프리미엄 카드에서 왔고 이미 프리미엄이면 안내만 한다.
     */
    fun checkPremium(email: String?, forBackup: Boolean) {
        viewModelScope.launch {
            val result = repository.isPremium(email)
            if (result is Success && result.data) {
                if (forBackup) {
                    _premiumEvent.value = Event(Unit)
                } else {
                    message(R.string.apply_ad_removal_function)
                }
            } else {
                _purchaseEvent.value = Event(Unit)
            }
        }
    }

    fun handleReviewSuccess() {
        message(R.string.thank_you_for_feedback)
    }
}
