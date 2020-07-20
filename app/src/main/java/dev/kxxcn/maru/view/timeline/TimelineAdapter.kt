package dev.kxxcn.maru.view.timeline

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TimelineAdapter(
    private val viewModel: TimelineViewModel
) : ListAdapter<TimelineItem, TimelineViewHolder>(TimelineCallback()) {

    private val releasable = mutableListOf<() -> Unit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        return TimelineViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val item = getItem(position)
        releasable.add(holder.bind(viewModel, item))
    }

    override fun onViewAttachedToWindow(holder: TimelineViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: TimelineViewHolder) {
        holder.onDetach()
        super.onViewDetachedFromWindow(holder)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        release()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    private fun release() {
        releasable.forEach { release -> release() }
        releasable.clear()
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
