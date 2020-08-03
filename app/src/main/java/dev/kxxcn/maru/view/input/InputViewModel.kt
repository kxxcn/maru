package dev.kxxcn.maru.view.input

import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Account
import dev.kxxcn.maru.data.Result.Success
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.succeeded
import dev.kxxcn.maru.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.maru.util.KEY_IS_PREMIUM
import dev.kxxcn.maru.util.KEY_TASK_ID
import dev.kxxcn.maru.util.extension.moneyToLong
import dev.kxxcn.maru.util.preference.PreferenceUtils
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class InputViewModel @AssistedInject constructor(
        private val repository: DataRepository,
        @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<InputViewModel>

    private var taskId: String? = null

    private val decimalFormat = NumberFormat.getInstance(Locale.KOREA)

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _closeEvent = MutableLiveData<Unit>()
    val closeEvent: LiveData<Unit> = _closeEvent

    private val _adEvent = MutableLiveData<Event<Unit>>()
    val adEvent: LiveData<Event<Unit>> = _adEvent

    private val _doneEvent = MutableLiveData<Event<Unit>>()
    val doneEvent: LiveData<Event<Unit>> = _doneEvent

    private val _taskName = MutableLiveData<String>()
    val taskName: LiveData<String> = _taskName

    private val _taskHusband = MutableLiveData<String>()
    val taskHusband: LiveData<String> = _taskHusband

    private val _taskWife = MutableLiveData<String>()
    val taskWife: LiveData<String> = _taskWife

    private val _taskRemain = MutableLiveData<String>()
    val taskRemain: LiveData<String> = _taskRemain

    val taskTotal: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(taskHusband) {
            val husband = taskHusband.value.moneyToLong()
            val wife = taskWife.value.moneyToLong()
            value = decimalFormat.format(husband + wife)
        }
        addSource(taskWife) {
            val husband = taskHusband.value.moneyToLong()
            val wife = taskWife.value.moneyToLong()
            value = decimalFormat.format(husband + wife)
        }
    }

    private val _unitType = MutableLiveData<Int>()
    val unitType: LiveData<Int> = _unitType

    private val _selectDrawableRes = MutableLiveData<Int>()
    val selectDrawableRes: LiveData<Int> = _selectDrawableRes

    private val _deselectDrawableRes = MutableLiveData<Int>()
    val deselectDrawableRes: LiveData<Int> = _deselectDrawableRes

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    val selectedFontColorRes = android.R.color.black
    val deselectedFontColorRes = if (PreferenceUtils.useDarkMode) R.color.maruFontColorNight else android.R.color.black

    init {
        _unitType.value = PreferenceUtils.unitType
        _selectDrawableRes.value = R.drawable.input_unit_select
        _deselectDrawableRes.value = R.drawable.input_unit_deselect
        _progress.value = false
        start(savedStateHandle.get(KEY_TASK_ID))
    }

    private fun start(taskId: String?) {
        taskId ?: return
        viewModelScope.launch {
            val result = repository.getTaskDetail(taskId)
            if (result is Success) {
                with(result.data) {
                    _taskName.value = task?.name
                    val husband = account?.husband ?: 0
                    val wife = account?.wife ?: 0
                    val remain = account?.remain ?: 0
                    _taskHusband.value = decimalFormat.format(husband)
                    _taskWife.value = decimalFormat.format(wife)
                    _taskRemain.value = decimalFormat.format(remain)
                }
            }
        }
        this.taskId = taskId
    }

    private fun getUnit(): Int? {
        val id = unitType.value ?: return null
        val type = InputFilterType.values()[id]
        return type.unit
    }

    fun increment(moneyType: InputMoneyType) {
        when (moneyType) {
            InputMoneyType.HUSBAND -> taskHusband.value.moneyToLong() to _taskHusband
            InputMoneyType.WIFE -> taskWife.value.moneyToLong() to _taskWife
            InputMoneyType.REMAIN -> taskRemain.value.moneyToLong() to _taskRemain
        }.also { (money, liveData) ->
            val unit = getUnit() ?: return
            val calc = money + unit
            liveData.value = decimalFormat.format(calc)
        }
    }

    fun decrement(moneyType: InputMoneyType) {
        when (moneyType) {
            InputMoneyType.HUSBAND -> taskHusband.value.moneyToLong() to _taskHusband
            InputMoneyType.WIFE -> taskWife.value.moneyToLong() to _taskWife
            InputMoneyType.REMAIN -> taskRemain.value.moneyToLong() to _taskRemain
        }.also { (money, liveData) ->
            val unit = getUnit() ?: return
            val calc = money - unit
            if (money <= 0 || calc < 0) return
            liveData.value = decimalFormat.format(calc)
        }
    }

    fun handleUnitSelection(type: InputFilterType) {
        PreferenceUtils.unitType = type.id
        _unitType.value = type.id
    }

    fun close() {
        _closeEvent.value = Unit
    }

    fun complete() {
        val id = taskId ?: return
        _progress.value = true
        viewModelScope.launch {
            val husband = taskHusband.value.moneyToLong().takeIf { it > 0 }
            val wife = taskWife.value.moneyToLong().takeIf { it > 0 }
            val remain = taskRemain.value.moneyToLong()
            if (husband == null && wife == null) {
                _snackbarText.value = Event(R.string.status_should_input_either)
                _progress.value = false
            } else {
                val result = repository.saveAccount(
                        Account(
                                taskId = id,
                                husband = husband ?: 0,
                                wife = wife ?: 0,
                                remain = remain,
                                date = Calendar.getInstance().timeInMillis
                        )
                )
                if (result.succeeded) {
                    if (savedStateHandle.get<Boolean>(KEY_IS_PREMIUM) == true) {
                        _doneEvent
                    } else {
                        _adEvent
                    }.also {
                        it.value = Event(Unit)
                    }
                }
            }
        }
    }
}
