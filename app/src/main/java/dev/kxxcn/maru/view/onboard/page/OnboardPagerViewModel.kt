package dev.kxxcn.maru.view.onboard.page

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.util.ANIMATION_ONBOARD_ACCOUNT
import dev.kxxcn.maru.util.ANIMATION_ONBOARD_TASKS
import dev.kxxcn.maru.util.ANIMATION_ONBOARD_WELCOME
import dev.kxxcn.maru.view.onboard.page.OnboardPagerFilterType.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OnboardPagerViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _onboardFileName = MutableLiveData<String>()
    val onboardFileName: LiveData<String> = _onboardFileName

    private val _onboardSubject = MutableLiveData<Int>()
    val onboardSubject: LiveData<Int> = _onboardSubject

    private val _onboardContent = MutableLiveData<Int>()
    val onboardContent: LiveData<Int> = _onboardContent

    private val _remaining = MutableLiveData<Long>()
    val remaining: LiveData<Long> = _remaining

    init {
        computeRemaining()
    }

    fun setFiltering(requestType: OnboardPagerFilterType) {
        when (requestType) {
            ONBOARD_TASKS -> setFilter(
                ANIMATION_ONBOARD_TASKS,
                R.string.onboard_tasks_subject,
                R.string.onboard_tasks_content
            )
            ONBOARD_ACCOUNT -> setFilter(
                ANIMATION_ONBOARD_ACCOUNT,
                R.string.onboard_account_subject,
                R.string.onboard_account_content
            )
            ONBOARD_WELCOME -> setFilter(
                ANIMATION_ONBOARD_WELCOME,
                R.string.onboard_welcome_subject,
                R.string.onboard_welcome_content
            )
        }
    }

    private fun setFilter(
        onboardFilename: String,
        @StringRes onboardSubjectStringRes: Int,
        @StringRes onboardContentStringRes: Int
    ) {
        _onboardFileName.value = onboardFilename
        _onboardSubject.value = onboardSubjectStringRes
        _onboardContent.value = onboardContentStringRes
    }

    private fun computeRemaining() {
        viewModelScope.launch {
            repository.getUsers().let { result ->
                if (result is Success) {
                    val remain = result.data[0].wedding - System.currentTimeMillis()
                    _remaining.value = TimeUnit.MILLISECONDS.toDays(remain) + 1
                }
            }
        }
    }
}
