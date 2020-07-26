package dev.kxxcn.maru.view.sort

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.data.Task
import dev.kxxcn.maru.databinding.SortItemBinding
import dev.kxxcn.maru.util.ItemTouchHelperCallback
import dev.kxxcn.maru.util.OnStartDragListener
import dev.kxxcn.maru.view.base.LifecycleAdapter
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import org.jetbrains.anko.sdk27.coroutines.onLongClick
import java.lang.ref.WeakReference
import java.util.*

class SortAdapter(
    private val viewModel: SortViewModel
) : LifecycleAdapter<Task?, SortAdapter.SortViewHolder>(SortDiffCallback()),
    ItemTouchHelperCallback.ItemTouchHelperAdapter,
    OnStartDragListener {

    private lateinit var refRecyclerView: WeakReference<RecyclerView>

    private lateinit var touchHelper: ItemTouchHelper

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        refRecyclerView = WeakReference(recyclerView)
        val callback = ItemTouchHelperCallback(this)
        touchHelper = ItemTouchHelper(callback).apply {
            attachToRecyclerView(recyclerView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortViewHolder {
        return SortViewHolder.from(parent, this)
    }

    override fun onBindViewHolder(h: SortViewHolder, position: Int) {
        val item = getItem(position) ?: return
        releasable.add(h.bind(viewModel, item))
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val tasks = viewModel.tasks.value ?: return
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(tasks, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(tasks, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemSelected(viewHolder: RecyclerView.ViewHolder?) {

    }

    override fun onItemClear(viewHolder: RecyclerView.ViewHolder?) {

    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }

    class SortViewHolder(
        private val binding: SortItemBinding,
        private val dragListener: OnStartDragListener
    ) : LifecycleViewHolder(binding) {

        init {
            binding.sortIcon.onLongClick {
                dragListener.onStartDrag(this@SortViewHolder)
            }
        }

        fun bind(viewModel: SortViewModel, task: Task): () -> Unit {
            with(binding) {
                this.lifecycleOwner = this@SortViewHolder
                this.viewModel = viewModel
                this.task = task
                this.executePendingBindings()
            }
            return { release() }
        }

        private fun release() {
            binding.sortParent.setOnClickListener(null)
        }

        companion object {

            fun from(parent: ViewGroup, dragListener: OnStartDragListener): SortViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SortItemBinding.inflate(layoutInflater, parent, false)
                return SortViewHolder(binding, dragListener)
            }
        }
    }
}

class SortDiffCallback : DiffUtil.ItemCallback<Task?>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}
