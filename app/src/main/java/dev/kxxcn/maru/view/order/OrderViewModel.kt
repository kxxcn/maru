package dev.kxxcn.maru.view.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.view.order.OrderFilterType.*

class OrderViewModel : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _filterType = MutableLiveData<OrderFilterType>()
    val filterType: LiveData<OrderFilterType> = _filterType

    val items: LiveData<List<OrderItem>> = filterType.map {
        when (it) {
            BASIC -> basicItems()
            CATHEDRAL -> cathedralItems()
            NO_OFFICIATE -> noOfficiateItems()
        }
    }

    init {
        _filterType.value = BASIC
    }

    fun setFiltering(requestType: OrderFilterType) {
        if (filterType.value == requestType) return
        _filterType.value = requestType
    }

    fun close() {
        _closeEvent.value = Event(Unit)
    }
}
