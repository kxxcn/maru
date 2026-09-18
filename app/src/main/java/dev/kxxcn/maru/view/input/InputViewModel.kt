package dev.kxxcn.maru.view.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Account
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.succeeded
import dev.kxxcn.maru.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.maru.util.COMPLETED_TASK
import dev.kxxcn.maru.util.DateUtils
import dev.kxxcn.maru.util.KEY_IS_PREMIUM
import dev.kxxcn.maru.util.KEY_TASK_ID
import dev.kxxcn.maru.util.extension.moneyToLong
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.BaseViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class InputViewModel @AssistedInject constructor(
    private val repository: DataRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    @AssistedFactory
    interface Factory : AssistedSavedStateViewModelFactory<InputViewModel>

    private var taskId: String? = null
    private var accountId: String? = null

    private val decimalFormat = NumberFormat.getInstance(Locale.KOREA)

    private val _adEvent = MutableLiveData<Event<Unit>>()
    val adEvent: LiveData<Event<Unit>> = _adEvent

    private val _doneEvent = MutableLiveData<Event<Unit>>()
    val doneEvent: LiveData<Event<Unit>> = _doneEvent

    private val _taskName = MutableLiveData<String>()
    val taskName: LiveData<String> = _taskName

    private val _taskIcon = MutableLiveData<String?>()
    val taskIcon: LiveData<String?> = _taskIcon

    private val _recordedDate = MutableLiveData<Long?>()
    val recordedDate: LiveData<Long?> = _recordedDate

    private val _selectedDate = MutableLiveData(startOfToday())
    val selectedDate: LiveData<Long> = _selectedDate

    val selectedDateText: LiveData<String> = _selectedDate.map {
        DateUtils.DATE_FORMAT_8.format(it)
    }

    private val _taskHusband = MutableLiveData(decimalFormat.format(0))
    val taskHusband: LiveData<String> = _taskHusband

    private val _taskWife = MutableLiveData(decimalFormat.format(0))
    val taskWife: LiveData<String> = _taskWife

    private val _taskRemain = MutableLiveData(decimalFormat.format(0))
    val taskRemain: LiveData<String> = _taskRemain

    private val _memo = MutableLiveData("")
    val memo: LiveData<String> = _memo

    val taskTotal: LiveData<String> = MediatorLiveData<String>().apply {
        fun update() {
            value = decimalFormat.format(taskHusband.value.moneyToLong() + taskWife.value.moneyToLong())
        }
        addSource(taskHusband) { update() }
        addSource(taskWife) { update() }
    }

    private val _selectedField = MutableLiveData(InputMoneyType.HUSBAND)
    val selectedField: LiveData<InputMoneyType> = _selectedField

    private val _autoComplete = MutableLiveData(PreferenceUtils.autoComplete)
    val autoComplete: LiveData<Boolean> = _autoComplete

    private val _progress = MutableLiveData(false)
    val progress: LiveData<Boolean> = _progress

    init {
        start(savedStateHandle.get(KEY_TASK_ID))
    }

    private fun start(taskId: String?) {
        taskId ?: return
        viewModelScope.launch {
            val result = repository.getTaskDetail(taskId)
            if (result is Success) {
                with(result.data) {
                    _taskName.value = task?.name
                    _taskIcon.value = task?.iconId
                    accountId = account?.id
                    _recordedDate.value = account?.date
                    _selectedDate.value = account?.date?.let(::startOfDay) ?: startOfToday()
                    _taskHusband.value = decimalFormat.format(account?.husband ?: 0L)
                    _taskWife.value = decimalFormat.format(account?.wife ?: 0L)
                    _taskRemain.value = decimalFormat.format(account?.remain ?: 0L)
                    _memo.value = account?.memo.orEmpty()
                }
            }
        }
        this.taskId = taskId
    }

    fun selectDate(timeInMillis: Long) {
        _selectedDate.value = startOfDay(timeInMillis)
    }

    fun setMemo(value: String) {
        _memo.value = value
    }

    fun selectField(type: InputMoneyType) {
        _selectedField.value = type
    }

    fun setAmount(type: InputMoneyType, input: String) {
        val digits = input.filter(Char::isDigit)
        if (digits.isEmpty()) {
            target(type).value = ""
            return
        }
        val value = digits.toLongOrNull() ?: return
        target(type).value = if (value > MAX_AMOUNT) {
            decimalFormat.format(MAX_AMOUNT)
        } else {
            decimalFormat.format(value)
        }
    }

    fun appendDigit(digit: String) {
        val next = ("${currentValue()}$digit").toLongOrNull() ?: return
        if (next > MAX_AMOUNT) return
        setValue(next)
    }

    fun deleteDigit() {
        setValue(currentValue() / 10)
    }

    fun addUnit(type: InputFilterType) {
        PreferenceUtils.unitType = type.id
        val next = currentValue() + type.unit
        if (next > MAX_AMOUNT) return
        setValue(next)
    }

    fun setAutoComplete(enabled: Boolean) {
        if (_autoComplete.value == enabled) return
        PreferenceUtils.autoComplete = enabled
        _autoComplete.value = enabled
    }

    private fun target(): MutableLiveData<String> {
        return target(_selectedField.value ?: InputMoneyType.HUSBAND)
    }

    private fun target(type: InputMoneyType): MutableLiveData<String> {
        return when (type) {
            InputMoneyType.HUSBAND -> _taskHusband
            InputMoneyType.WIFE -> _taskWife
            InputMoneyType.REMAIN -> _taskRemain
        }
    }

    private fun currentValue(): Long = target().value.moneyToLong()

    private fun setValue(value: Long) {
        target().value = decimalFormat.format(value.coerceAtLeast(0L))
    }

    fun complete() {
        val id = taskId ?: return
        _progress.value = true
        viewModelScope.launch {
            val husband = taskHusband.value.moneyToLong().takeIf { it > 0 }
            val wife = taskWife.value.moneyToLong().takeIf { it > 0 }
            val remain = taskRemain.value.moneyToLong()
            if (husband == null && wife == null) {
                message(R.string.status_should_input_either)
                _progress.value = false
            } else {
                val result = repository.saveAccount(
                    Account(
                        id = accountId ?: UUID.randomUUID().toString(),
                        taskId = id,
                        husband = husband ?: 0,
                        wife = wife ?: 0,
                        remain = remain,
                        date = selectedDate.value ?: startOfToday(),
                        memo = memo.value.orEmpty().trim()
                    )
                )
                if (result.succeeded) {
                    if ((autoComplete.value ?: true) && remain == 0L) {
                        repository.updateTask(id, COMPLETED_TASK)
                    }
                    if (savedStateHandle.get<Boolean>(KEY_IS_PREMIUM) == true) {
                        _doneEvent
                    } else {
                        _adEvent
                    }.also {
                        it.value = Event(Unit)
                    }
                } else {
                    _progress.value = false
                }
            }
        }
    }

    companion object {
        private const val MAX_AMOUNT = 999_999_999_999L

        private fun startOfToday(): Long = startOfDay(System.currentTimeMillis())

        private fun startOfDay(timeInMillis: Long): Long = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}
