package dev.kxxcn.maru.view.tasks.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.databinding.TasksEmptyItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.tasks.TasksAdapter

class TasksEmptyHolder(
    private val binding: TasksEmptyItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(item: TasksAdapter.TasksItem) {
        with(binding) {
            this.lifecycleOwner = this@TasksEmptyHolder
            this.stringRes = item.stringRes
            this.executePendingBindings()
        }
    }

    companion object {
        fun from(parent: ViewGroup): TasksEmptyHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TasksEmptyItemBinding.inflate(inflater, parent, false)
            return TasksEmptyHolder(binding)
        }
    }
}
