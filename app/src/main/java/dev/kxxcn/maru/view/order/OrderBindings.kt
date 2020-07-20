package dev.kxxcn.maru.view.order

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.view.custom.UnderlineTextView

@BindingAdapter("app:underlineSelector")
fun setUnderlineSelector(view: UnderlineTextView, isSelect: Boolean) {
    view.handleFilterSelection(isSelect)
}

@BindingAdapter("app:orderItems")
fun setOrderItems(view: RecyclerView, items: List<OrderItem>?) {
    items?.let {
        with(view) {
            (adapter as? OrderAdapter)?.submitList(it)
            scheduleLayoutAnimation()
        }
    }
}
