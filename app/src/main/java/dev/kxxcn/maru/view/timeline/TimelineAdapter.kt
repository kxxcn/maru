package dev.kxxcn.maru.view.timeline

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import dev.kxxcn.maru.view.base.LifecycleAdapter

class TimelineAdapter(
    private val viewModel: TimelineViewModel
) : LifecycleAdapter<TimelineItem, TimelineViewHolder>(TimelineCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        return TimelineViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val item = getItem(position)
        releasable.add(holder.bind(viewModel, item))
    }
}

class TimelineCallback : DiffUtil.ItemCallback<TimelineItem>() {

    override fun areItemsTheSame(oldItem: TimelineItem, newItem: TimelineItem): Boolean {
        return oldItem.days == newItem.days
    }

    override fun areContentsTheSame(oldItem: TimelineItem, newItem: TimelineItem): Boolean {
        return oldItem == newItem
    }
}
