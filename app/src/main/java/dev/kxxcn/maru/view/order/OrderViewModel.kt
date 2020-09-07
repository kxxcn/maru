package dev.kxxcn.maru.view.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import dev.kxxcn.maru.view.base.BaseViewModel
import dev.kxxcn.maru.view.order.OrderFilterType.*

class OrderViewModel : BaseViewModel() {

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
}
