package dev.kxxcn.maru.view.days

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import dev.kxxcn.maru.data.Day
import dev.kxxcn.maru.view.base.LifecycleAdapter

class DaysAdapter(
    private val delete: (Int) -> Unit
) : LifecycleAdapter<Day, DaysViewHolder>(DaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        return DaysViewHolder.from(parent, delete)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        holder.bind(getItem(position))
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
