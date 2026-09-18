package dev.kxxcn.maru.view.timeline

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import dev.kxxcn.maru.view.base.LifecycleAdapter

class TimelineAdapter(
    private val viewModel: TimelineViewModel
) : LifecycleAdapter<TimelineRow, TimelineViewHolder>(TimelineCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        return TimelineViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position), isLast = position == itemCount - 1)
    }
}

class TimelineCallback : DiffUtil.ItemCallback<TimelineRow>() {

    override fun areItemsTheSame(oldItem: TimelineRow, newItem: TimelineRow): Boolean {
        return oldItem.item.days == newItem.item.days
    }

    override fun areContentsTheSame(oldItem: TimelineRow, newItem: TimelineRow): Boolean {
        return oldItem == newItem
    }
}
