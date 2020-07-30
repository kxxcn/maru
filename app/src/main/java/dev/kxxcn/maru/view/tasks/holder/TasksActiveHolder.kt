package dev.kxxcn.maru.view.tasks.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.databinding.TasksActiveItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.tasks.TasksViewModel

class TasksActiveHolder(
    private val binding: TasksActiveItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(viewModel: TasksViewModel, item: TaskDetail?, isPremium: Boolean) {
        with(binding) {
            this.lifecycleOwner = this@TasksActiveHolder
            this.viewModel = viewModel
            this.item = item
            this.isPremium = isPremium
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): TasksActiveHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TasksActiveItemBinding.inflate(inflater, parent, false)
            return TasksActiveHolder(binding)
        }
    }
}
