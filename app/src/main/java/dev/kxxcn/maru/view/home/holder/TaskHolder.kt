package dev.kxxcn.maru.view.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.databinding.TaskItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.home.HomeAdapter

class TaskHolder(
    private val binding: TaskItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(item: HomeAdapter.SummaryItem) {
        with(binding) {
            this.lifecycleOwner = this@TaskHolder
            this.content = item.content
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): TaskHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TaskItemBinding.inflate(inflater, parent, false)
            return TaskHolder(binding)
        }
    }
}
