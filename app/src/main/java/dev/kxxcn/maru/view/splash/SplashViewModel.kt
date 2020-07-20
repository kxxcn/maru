package dev.kxxcn.maru.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.util.SPLASH_SCREEN_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _register = MutableLiveData<Boolean>()
    val register: LiveData<Boolean> = _register

    init {
        start()
    }

    private fun start() {
        viewModelScope.launch {
            delay(SPLASH_SCREEN_DELAY)
            repository.getUsers().let { result ->
                if (result is Success) {
                    _register.value = result.data.isNotEmpty()
                }
            }
        }
    }
}
