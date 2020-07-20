package dev.kxxcn.maru.view.order

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.GlideApp
import dev.kxxcn.maru.databinding.OrderItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder

class OrderHolder(
    private val binding: OrderItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(position: Int, item: OrderItem) {
        with(binding) {
            this.lifecycleOwner = this@OrderHolder
            this.position = position + 1
            this.item = item
            this.executePendingBindings()
        }
    }

    fun clear() {
        GlideApp
            .with(itemView.context)
            .clear(binding.orderImage)
    }

    companion object {

        fun from(parent: ViewGroup): OrderHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = OrderItemBinding.inflate(inflater, parent, false)
            return OrderHolder(binding)
        }
    }
}
