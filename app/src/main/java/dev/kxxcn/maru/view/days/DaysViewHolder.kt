package dev.kxxcn.maru.view.days

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.data.Day
import dev.kxxcn.maru.databinding.DayItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import org.jetbrains.anko.sdk27.coroutines.onLongClick

class DaysViewHolder(
    val binding: DayItemBinding,
    val delete: (Int) -> Unit
) : LifecycleViewHolder(binding) {

    init {
        itemView.onLongClick {
            val pos = adapterPosition
                .takeIf { it > -1 }
                ?: return@onLongClick
            delete(pos)
        }
    }

    fun bind(day: Day) {
        with(binding) {
            this.lifecycleOwner = this@DaysViewHolder
            this.day = day
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup, delete: (Int) -> Unit): DaysViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DayItemBinding.inflate(inflater, parent, false)
            return DaysViewHolder(binding, delete)
        }
    }
}
