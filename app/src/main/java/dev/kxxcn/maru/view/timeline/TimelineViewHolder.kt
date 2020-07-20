package dev.kxxcn.maru.view.timeline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.children
import dev.kxxcn.maru.databinding.TimelineItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.custom.TimelineView

class TimelineViewHolder(
    private val binding: TimelineItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(viewModel: TimelineViewModel, item: TimelineItem): () -> Unit {
        with(binding) {
            this.lifecycleOwner = this@TimelineViewHolder
            this.viewModel = viewModel
            this.item = item
            this.executePendingBindings()
        }
        return { release() }
    }

    private fun release() {
        with(binding.timelineTaskGroup) {
            for (child in children) (child as? TimelineView)?.release()
            removeAllViews()
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
