package dev.kxxcn.maru.view.tasks.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import dev.kxxcn.maru.R
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
            this.tasksChart.setHoleColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.tasksCompletedCardBackground
                )
            )
            this.tasksChart.animateY(1400, Easing.EaseInOutQuad)
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
