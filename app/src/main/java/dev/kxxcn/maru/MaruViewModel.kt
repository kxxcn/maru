package dev.kxxcn.maru

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.kxxcn.maru.util.BACK_BUTTON_DELAY
import dev.kxxcn.maru.view.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MaruViewModel : BaseViewModel() {

    private val _finishEvent = MutableLiveData<Event<Unit>>()
    val finishEvent: LiveData<Event<Unit>> = _finishEvent

    private val backPressEvents = MutableSharedFlow<Long>(extraBufferCapacity = 1)

    private var backPressedTimeMs = 0L

    init {
        observeChannel()
    }

    fun onBackPressed() {
        backPressEvents.tryEmit(System.currentTimeMillis())
    }

    private fun observeChannel() {
        viewModelScope.launch {
            backPressEvents
                .collect {
                    if (backPressedTimeMs == 0L) {
                        backPressedTimeMs = it
                        message(R.string.close_app_press_back_button_more)
                        return@collect
                    } else {
                        if (it - backPressedTimeMs < BACK_BUTTON_DELAY) {
                            _finishEvent.value = Event(Unit)
                        } else {
                            backPressedTimeMs = 0L
                            onBackPressed()
                        }
                    }
                }
        }
    }
}
