package dev.kxxcn.maru.view.present

import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Present
import dev.kxxcn.maru.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.maru.util.KEY_PRESENT_TYPE
import dev.kxxcn.maru.util.PAGER_CATEGORY
import dev.kxxcn.maru.util.PAGER_DESCRIPTION
import dev.kxxcn.maru.util.SCROLL_TO_TOP_DELAY
import dev.kxxcn.maru.view.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PresentViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<PresentViewModel>

    private val _scrollEvent = MutableLiveData<Event<Unit>>()
    val scrollEvent: LiveData<Event<Unit>> = _scrollEvent

    private val _present = MutableLiveData<Present?>()
    val present: LiveData<Present?> = _present

    val items: List<Present>?
        get() = (savedStateHandle.get(KEY_PRESENT_TYPE) as? PresentFilterType)?.items

    val categoryRes: Int
        get() {
            return when (savedStateHandle.get(KEY_PRESENT_TYPE) as? PresentFilterType) {
                PresentFilterType.RING -> R.string.more_contents_ring
                PresentFilterType.DRESS -> R.string.more_contents_dress
                PresentFilterType.TUXEDO -> R.string.more_contents_tuxedo
                else -> R.string.more_contents_hanbok
            }
        }

    val titleRes = present.map {
        it?.titleRes
    }

    val contentRes = present.map {
        it?.contentRes
    }

    val currentItem = MediatorLiveData<Int>().apply {
        addSource(present) { value = it?.let { PAGER_DESCRIPTION } ?: PAGER_CATEGORY }
    }

    init {
        currentItem.value = PAGER_CATEGORY
    }

    fun selection(present: Present) {
        _present.value = present
    }

    fun onBackPressed() {
        _present.value = null
    }

    fun scrollToTop() {
        viewModelScope.launch {
            delay(SCROLL_TO_TOP_DELAY)
            _scrollEvent.value = Event(Unit)
        }
    }
}
