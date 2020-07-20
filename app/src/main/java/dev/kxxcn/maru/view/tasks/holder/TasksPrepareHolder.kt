package dev.kxxcn.maru.view.tasks.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.databinding.TasksPrepareItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.tasks.TasksViewModel

class TasksPrepareHolder(
    private val binding: TasksPrepareItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(viewModel: TasksViewModel, item: TaskDetail?): () -> Unit {
        with(binding) {
            this.lifecycleOwner = this@TasksPrepareHolder
            this.viewModel = viewModel
            this.item = item
            this.executePendingBindings()
        }
        return { release() }
    }

    private fun release() {
        binding.tasksPrepareParent.setOnClickListener(null)
    }

    companion object {

        fun from(parent: ViewGroup): TasksPrepareHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TasksPrepareItemBinding.inflate(inflater, parent, false)
            return TasksPrepareHolder(binding)
        }
    }
}
