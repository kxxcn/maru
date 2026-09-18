package dev.kxxcn.maru

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.os.SystemClock
import dev.kxxcn.maru.util.BACK_BUTTON_DELAY
import dev.kxxcn.maru.view.base.BaseViewModel

class MaruViewModel : BaseViewModel() {

    private val _finishEvent = MutableLiveData<Event<Unit>>()
    val finishEvent: LiveData<Event<Unit>> = _finishEvent

    private var backPressedTimeMs = 0L

    fun onBackPressed() {
        val currentTimeMs = SystemClock.elapsedRealtime()
        if (backPressedTimeMs != 0L && currentTimeMs - backPressedTimeMs < BACK_BUTTON_DELAY) {
            backPressedTimeMs = 0L
            _finishEvent.value = Event(Unit)
        } else {
            backPressedTimeMs = currentTimeMs
            message(R.string.close_app_press_back_button_more)
        }
    }
}
