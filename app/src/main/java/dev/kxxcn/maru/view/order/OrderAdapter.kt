package dev.kxxcn.maru.view.order

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class OrderAdapter : ListAdapter<OrderItem, OrderHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        return OrderHolder.from(parent)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.bind(position, getItem(position))
    }

    override fun onViewRecycled(holder: OrderHolder) {
        super.onViewRecycled(holder)
        holder.clear()
    }

    override fun onViewAttachedToWindow(holder: OrderHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: OrderHolder) {
        holder.onDetach()
        super.onViewDetachedFromWindow(holder)
    }
}

class OrderDiffCallback : DiffUtil.ItemCallback<OrderItem>() {

    override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
        return oldItem == newItem
    }
}
