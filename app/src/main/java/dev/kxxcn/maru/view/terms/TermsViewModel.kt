package dev.kxxcn.maru.view.terms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dev.kxxcn.maru.util.KEY_TERMS_TITLE
import dev.kxxcn.maru.util.KEY_TERMS_TYPE
import dev.kxxcn.maru.view.base.BaseViewModel

class TermsViewModel(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _titleRes = MutableLiveData<Int>()
    val titleRes: LiveData<Int> = _titleRes

    private val _contentRes = MutableLiveData<Int>()
    val contentRes: LiveData<Int> = _contentRes

    init {
        _contentRes.value = savedStateHandle.get<Int>(KEY_TERMS_TYPE)
        _titleRes.value = savedStateHandle.get<Int>(KEY_TERMS_TITLE)
    }
}
