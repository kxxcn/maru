package dev.kxxcn.maru.view.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import dev.kxxcn.maru.databinding.TimelineItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder

class TimelineViewHolder(
    private val binding: TimelineItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(viewModel: TimelineViewModel, row: TimelineRow, isLast: Boolean) {
        with(binding) {
            this.lifecycleOwner = this@TimelineViewHolder
            this.viewModel = viewModel
            this.row = row
            timelineRail.isVisible = !isLast
            this.executePendingBindings()
        }
    }

    companion object {
        fun from(parent: ViewGroup): TimelineViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TimelineItemBinding.inflate(inflater, parent, false)
            return TimelineViewHolder(binding)
        }
    }
}
