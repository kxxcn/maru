package dev.kxxcn.maru.view.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.children
import dev.kxxcn.maru.databinding.TaskItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.custom.TransactionView
import dev.kxxcn.maru.view.home.HomeAdapter

class TaskHolder(
    private val binding: TaskItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(item: HomeAdapter.SummaryItem): () -> Unit {
        with(binding) {
            this.lifecycleOwner = this@TaskHolder
            this.content = item.content
            this.executePendingBindings()
        }
        return { release() }
    }

    fun release() {
        with(binding.taskActivityList) {
            for (child in children) (child as? TransactionView)?.release()
            removeAllViews()
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
