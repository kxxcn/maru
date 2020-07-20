package dev.kxxcn.maru.view.days

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.kxxcn.maru.data.Day

class DaysAdapter(
    private val delete: (Int) -> Unit
) : ListAdapter<Day, DaysViewHolder>(DaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        return DaysViewHolder.from(parent, delete)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewAttachedToWindow(holder: DaysViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: DaysViewHolder) {
        holder.onDetach()
        super.onViewDetachedFromWindow(holder)
    }
}

class DaysDiffCallback : DiffUtil.ItemCallback<Day>() {

    override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem == newItem
    }
}
