package dev.kxxcn.maru.view.register

import androidx.annotation.StringRes
import androidx.lifecycle.*
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Task
import dev.kxxcn.maru.data.User
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.succeeded
import dev.kxxcn.maru.util.ConvertUtils
import dev.kxxcn.maru.util.ONE_THOUSAND_MILLION
import dev.kxxcn.maru.util.extension.msToDate
import dev.kxxcn.maru.view.register.RegisterFilterType.*
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val repository: DataRepository
) : ViewModel() {

    private val numberFormat = NumberFormat.getInstance(Locale.KOREA)

    private val _motion = MutableLiveData<Unit>()
    val motion: LiveData<Unit> = _motion

    private val _titleLabel = MutableLiveData<Int>()
    val titleLabel: LiveData<Int> = _titleLabel

    private val _currentHintLabel = MutableLiveData<Int>()
    val currentHintLabel: LiveData<Int> = _currentHintLabel

    private val _buttonLabel = MutableLiveData<Int>()
    val buttonLabel: LiveData<Int> = _buttonLabel

    private val _disableTextRes = MutableLiveData<Int>()
    val disableTextRes: LiveData<Int> = _disableTextRes

    private val _enableTextRes = MutableLiveData<Int>()
    val enableTextRes: LiveData<Int> = _enableTextRes

    private val _backPressedEvent = MutableLiveData<Unit>()
    val backPressedEvent: LiveData<Unit> = _backPressedEvent

    var infoText = MutableLiveData<String?>()

    val moneyText = infoText.map {
        val money = try {
            var input = it
                ?.replace(",", "")
                ?.toLong()
                ?: 0L
            if (input > ONE_THOUSAND_MILLION) {
                infoText.value = numberFormat.format(ONE_THOUSAND_MILLION)
                input = ONE_THOUSAND_MILLION
            }
            input
        } catch (e: Exception) {
            0L
        }

        ConvertUtils.moneyText(money)
    }

    val empty: LiveData<Boolean> = infoText.map {
        it.isNullOrEmpty()
    }

    private val _filterType = MutableLiveData<RegisterFilterType>()
    val isWedding: LiveData<Boolean> = _filterType.map {
        it == REGISTER_WEDDING
    }
    val isBudget: LiveData<Boolean> = _filterType.map {
        it == REGISTER_BUDGET
    }

    val today: String by lazy {
        Calendar.getInstance().timeInMillis.msToDate()
    }

    init {
        infoText.value = null
        _disableTextRes.value = R.drawable.register_next_button_disable
        _enableTextRes.value = R.drawable.register_next_button_enable
    }

    fun setFiltering(requestType: RegisterFilterType) {
        _filterType.value = requestType
        when (requestType) {
            REGISTER_NAME -> setFilter(
                R.string.register_name,
                R.string.register_name_hint,
                R.string.register_next
            )
            REGISTER_WEDDING -> setFilter(
                R.string.register_wedding,
                R.string.register_wedding_hint,
                R.string.register_next
            )
            REGISTER_BUDGET -> setFilter(
                R.string.register_budget,
                R.string.register_budget_hint,
                R.string.register_complete
            )
            REGISTER_COMPLETE -> return
            else -> {}
        }
    }

    fun setInputText(text: String?) {
        infoText.value = text
    }

    private fun setFilter(
        @StringRes titleLabelString: Int,
        @StringRes currentHintLabelString: Int,
        @StringRes buttonLabelString: Int
    ) {
        _titleLabel.value = titleLabelString
        _currentHintLabel.value = currentHintLabelString
        _buttonLabel.value = buttonLabelString
    }

    fun startAnimation() {
        _motion.value = Unit
    }

    fun backPressed() {
        _backPressedEvent.value = Unit
    }

    fun saveUser(user: User, items: List<Task>) {
        viewModelScope.launch {
            val result = repository.saveUser(user)
            if (result.succeeded) {
                repository.replaceTasks(items)
            }
        }
    }
}
