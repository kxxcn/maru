package dev.kxxcn.maru.view.sort

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.data.Task
import dev.kxxcn.maru.databinding.SortItemBinding
import dev.kxxcn.maru.util.OnStartDragListener
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import org.jetbrains.anko.sdk27.coroutines.onLongClick

class SortViewHolder(
        private val binding: SortItemBinding,
        private val dragListener: OnStartDragListener
) : LifecycleViewHolder(binding) {

    init {
        binding.sortIcon.onLongClick { dragListener.onStartDrag(this@SortViewHolder) }
    }

    fun bind(viewModel: SortViewModel, task: Task): () -> Unit {
        with(binding) {
            this.lifecycleOwner = this@SortViewHolder
            this.viewModel = viewModel
            this.task = task
            this.executePendingBindings()
        }
        return { binding.sortParent.setOnClickListener(null) }
    }

    companion object {

        fun from(parent: ViewGroup, dragListener: OnStartDragListener): SortViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SortItemBinding.inflate(layoutInflater, parent, false)
            return SortViewHolder(binding, dragListener)
        }
    }
}
