package dev.kxxcn.maru

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kxxcn.maru.util.BACK_BUTTON_DELAY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class MaruViewModel : ViewModel() {

    private val _finishEvent = MutableLiveData<Event<Unit>>()
    val finishEvent: LiveData<Event<Unit>> = _finishEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val channel = BroadcastChannel<Long>(1)

    private var backPressedTimeMs = 0L

    init {
        observeChannel()
    }

    fun onBackPressed() {
        viewModelScope.launch { channel.send(System.currentTimeMillis()) }
    }

    private fun observeChannel() {
        viewModelScope.launch {
            channel
                .asFlow()
                .collect {
                    if (backPressedTimeMs == 0L) {
                        backPressedTimeMs = it
                        _snackbarText.value = Event(R.string.close_app_press_back_button_more)
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
