package dev.kxxcn.maru.view.purchase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.Purchase
import com.google.firebase.auth.FirebaseAuth
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.source.DataRepository
import dev.kxxcn.maru.data.succeeded
import dev.kxxcn.maru.view.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class PurchaseViewModel @Inject constructor(
    private val repository: DataRepository,
    private val auth: FirebaseAuth
) : BaseViewModel() {

    private val _purchaseEvent = MutableLiveData<Event<Unit>>()
    val purchaseEvent: LiveData<Event<Unit>> = _purchaseEvent

    private val _successEvent = MutableLiveData<Event<Int>>()
    val successEvent: LiveData<Event<Int>> = _successEvent

    private val _failureEvent = MutableLiveData<Event<Int>>()
    val failureEvent: LiveData<Event<Int>> = _failureEvent

    fun purchase() {
        _purchaseEvent.value = Event(Unit)
    }

    fun handlePurchaseSuccess(purchases: List<Purchase>?) {
        viewModelScope.launch {
            val (liveData, messageRes) = purchases
                ?.firstOrNull()
                ?.let { repository.savePremium(auth.currentUser?.email, it) }
                ?.takeIf { it.succeeded }
                ?.run { _successEvent to R.string.purchase_premium }
                ?: _failureEvent to 0
            liveData.value = Event(messageRes)
        }
    }

    fun handlePurchaseFailure() {
        message(R.string.try_again_later)
    }
}
