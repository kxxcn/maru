package dev.kxxcn.maru.view.days

import androidx.lifecycle.*
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Day
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.succeeded
import dev.kxxcn.maru.util.ColorUtils
import dev.kxxcn.maru.util.ConvertUtils
import dev.kxxcn.maru.util.DateUtils
import dev.kxxcn.maru.util.extension.msToDate
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.days.DaysFilterType.COUNT
import dev.kxxcn.maru.view.days.DaysFilterType.REMAIN
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DaysAddViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _datePickerEvent = MutableLiveData<Event<Unit>>()
    val datePickerEvent: LiveData<Event<Unit>> = _datePickerEvent

    private val _timeText = MutableLiveData<String>()
    val timeText: LiveData<String> = _timeText

    private val _selectCount = MutableLiveData<Boolean>().apply {
        value = true
    }
    private val _selectRemain = MutableLiveData<Boolean>().apply {
        value = false
    }

    val selectCount = MediatorLiveData<Boolean>().apply {
        addSource(_selectCount) { value = it }
        addSource(_selectRemain) { value = !it }
    }
    val selectRemain = MediatorLiveData<Boolean>().apply {
        addSource(_selectRemain) { value = it }
        addSource(_selectCount) { value = !it }
    }

    private val _dayStringRes = MutableLiveData<Int>()
    val dayStringRes: LiveData<Int> = _dayStringRes

    private val _dayCount = MutableLiveData<Int>()
    val dayCount: LiveData<Int> = _dayCount

    val whiteColorRes = android.R.color.white
    val blackColorRes =
        if (PreferenceUtils.useDarkMode) R.color.maruFontColorNight else android.R.color.black
    val grayColorRes = android.R.color.darker_gray

    val content = MutableLiveData<String>()

    init {
        start()
    }

    private fun start() {
        val timeMs = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        setTime(timeMs, true)
    }

    fun setTime(timeMs: Long, forceCount: Boolean = false) {
        _timeText.value = timeMs.msToDate(
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            )
        )
        setDay(timeMs, forceCount)
    }

    fun setDay(timeMs: Long, forceCount: Boolean = false) {
        if (forceCount || selectCount.value == true) {
            ConvertUtils.getCount(timeMs)
        } else {
            ConvertUtils.getRemain(timeMs)
        }.also { (stringRes, count) ->
            _dayStringRes.value = stringRes
            _dayCount.value = count
        }
    }

    fun close() {
        _closeEvent.value = Event(Unit)
    }

    fun register() {
        val content = content.value
        if (content.isNullOrEmpty()) {
            _snackbarText.value = Event(R.string.days_add_hint)
        } else {
            val timeText = timeText.value ?: return
            val timeMs = DateUtils.DATE_FORMAT_4
                .parse(timeText)
                ?.time
                ?: return

            viewModelScope.launch {
                val result = repository.saveDay(
                    Day(
                        content = content,
                        date = timeMs,
                        type = if (selectCount.value == true) COUNT else REMAIN,
                        color = ColorUtils.getDaysColor()
                    )
                )
                if (result.succeeded) {
                    close()
                }
            }
        }
    }

    fun setFiltering(filterType: DaysFilterType) {
        when (filterType) {
            COUNT -> _selectCount.value = true
            REMAIN -> _selectRemain.value = true
        }
    }

    fun datePicker() {
        _datePickerEvent.value = Event(Unit)
    }
}
