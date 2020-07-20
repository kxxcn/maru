package dev.kxxcn.maru.view.onboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dev.kxxcn.maru.R
import dev.kxxcn.maru.view.onboard.page.OnboardPagerFilterType.ONBOARD_TASKS
import dev.kxxcn.maru.view.onboard.page.OnboardPagerFilterType.ONBOARD_WELCOME

class OnboardViewModel : ViewModel() {

    val currentItem = MutableLiveData<Int>()

    val isLastPage: LiveData<Boolean> = currentItem.map { it == 2 }

    private val _nextLabel = MutableLiveData<Int>()
    val nextLabel: LiveData<Int> = _nextLabel

    private val _nextDrawable = MutableLiveData<Int>()
    val nextDrawable: LiveData<Int> = _nextDrawable

    private val _completeLabel = MutableLiveData<Int>()
    val completeLabel: LiveData<Int> = _completeLabel

    private val _completeDrawable = MutableLiveData<Int>()
    val completeDrawable: LiveData<Int> = _completeDrawable

    private val _popBackStack = MutableLiveData<Unit>()
    val popBackStack: LiveData<Unit> = _popBackStack

    init {
        currentItem.value = 0
        _nextLabel.value = R.string.onboard_next
        _completeLabel.value = R.string.onboard_complete
        _nextDrawable.value = R.drawable.onboard_next_button
        _completeDrawable.value = R.drawable.onboard_complete_button
    }

    fun onNext() {
        if (currentItem.value == ONBOARD_WELCOME.ordinal) {
            _popBackStack.value = Unit
        } else {
            currentItem.value = currentItem.value?.plus(1)
        }
    }

    fun onPrevious() {
        if (currentItem.value == ONBOARD_TASKS.ordinal) {
            _popBackStack.value = Unit
        } else {
            currentItem.value = currentItem.value?.minus(1)
        }
    }
}
