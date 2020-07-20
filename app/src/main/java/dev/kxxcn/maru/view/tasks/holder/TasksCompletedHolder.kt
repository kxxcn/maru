package dev.kxxcn.maru.view.tasks.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.databinding.TasksCompletedItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.tasks.TasksViewModel

class TasksCompletedHolder(
    private val binding: TasksCompletedItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(viewModel: TasksViewModel, item: TaskDetail?) {
        with(binding) {
            this.lifecycleOwner = this@TasksCompletedHolder
            this.viewModel = viewModel
            this.item = item
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): TasksCompletedHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TasksCompletedItemBinding.inflate(inflater, parent, false)
            return TasksCompletedHolder(binding)
        }
    }
}
