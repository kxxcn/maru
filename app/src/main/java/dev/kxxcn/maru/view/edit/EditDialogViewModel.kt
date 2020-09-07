package dev.kxxcn.maru.view.edit

import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.succeeded
import dev.kxxcn.maru.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.maru.util.ConvertUtils
import dev.kxxcn.maru.util.KEY_REGISTER_TYPE
import dev.kxxcn.maru.util.ONE_THOUSAND_MILLION
import dev.kxxcn.maru.view.base.BaseViewModel
import dev.kxxcn.maru.view.register.RegisterFilterType
import dev.kxxcn.maru.view.register.RegisterFilterType.*
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class EditDialogViewModel @AssistedInject constructor(
    private val repository: DataRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<EditDialogViewModel>

    private val numberFormat = NumberFormat.getInstance(Locale.KOREA)

    private val _titleRes = MutableLiveData<Int>()
    val titleRes: LiveData<Int> = _titleRes

    private val _hintRes = MutableLiveData<Int>()
    val hintRes: LiveData<Int> = _hintRes

    val content = MutableLiveData<String?>()

    val moneyText = content.map {
        val money = try {
            var input = it
                ?.replace(",", "")
                ?.toLong()
                ?: 0L
            if (input > ONE_THOUSAND_MILLION && isBudget.value == true) {
                content.value = numberFormat.format(ONE_THOUSAND_MILLION)
                input = ONE_THOUSAND_MILLION
            }
            input
        } catch (e: Exception) {
            0L
        }

        ConvertUtils.moneyText(money)
    }

    val isContentFull = content.map {
        it?.isNotEmpty()
    }

    val enableButtonRes = R.drawable.edit_dialog_enable_button
    val disableButtonRes = R.drawable.edit_dialog_disable_button

    val isBudget = MutableLiveData<Boolean>().apply {
        value =
            savedStateHandle.get<RegisterFilterType>(KEY_REGISTER_TYPE) == REGISTER_BUDGET
    }

    init {
        when (savedStateHandle.get<RegisterFilterType>(KEY_REGISTER_TYPE)) {
            REGISTER_NAME -> R.string.edit_dialog_title_name to R.string.edit_dialog_title_name_hint
            REGISTER_WEDDING -> R.string.edit_dialog_title_wedding to R.string.edit_dialog_title_wedding_hint
            REGISTER_BUDGET -> R.string.edit_dialog_title_budget to R.string.edit_dialog_title_budget_hint
            REGISTER_COMPLETE,
            null -> throw RuntimeException("Invalid Filter Type.")
        }.also { (title, hint) ->
            _titleRes.value = title
            _hintRes.value = hint
        }

        content.value = null
    }

    fun save() {
        val content = content.value ?: return
        viewModelScope.launch {
            val result = when (savedStateHandle.get<RegisterFilterType>(KEY_REGISTER_TYPE)) {
                REGISTER_NAME -> repository.editName(content)
                REGISTER_BUDGET -> repository.editBudget(content.replace(",", "").toLong())
                REGISTER_WEDDING,
                REGISTER_COMPLETE,
                null -> throw RuntimeException("Invalid Filter Type.")
            }
            if (result.succeeded) {
                close()
            }
        }
    }
}
