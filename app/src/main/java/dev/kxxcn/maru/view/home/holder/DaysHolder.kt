package dev.kxxcn.maru.view.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.children
import dev.kxxcn.maru.databinding.DaysItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.custom.DayView
import dev.kxxcn.maru.view.home.HomeAdapter

class DaysHolder(
    private val binding: DaysItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(item: HomeAdapter.SummaryItem): () -> Unit {
        with(binding) {
            this.lifecycleOwner = this@DaysHolder
            this.content = item.content
            this.executePendingBindings()
        }
        return { release() }
    }

    fun release() {
        with(binding.dayList) {
            for (child in children) (child as? DayView)?.release()
            removeAllViews()
        }
    }

    companion object {

        fun from(parent: ViewGroup): DaysHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DaysItemBinding.inflate(inflater, parent, false)
            return DaysHolder(binding)
        }
    }
}
