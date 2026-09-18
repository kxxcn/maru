package dev.kxxcn.maru.view.tasks

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.view.base.LifecycleAdapter
import dev.kxxcn.maru.view.tasks.holder.TasksEmptyHolder
import dev.kxxcn.maru.view.tasks.holder.TasksNativeAdHolder
import dev.kxxcn.maru.view.tasks.holder.TasksRowHolder

class TasksAdapter(
    private val viewModel: TasksViewModel,
    private val requestManager: RequestManager
) : LifecycleAdapter<TasksAdapter.TasksItem, RecyclerView.ViewHolder>(TasksDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ROW -> TasksRowHolder.from(parent)
            TYPE_AD -> TasksNativeAdHolder.from(parent, requestManager)
            else -> TasksEmptyHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.viewType) {
            TYPE_ROW -> {
                val h = holder as? TasksRowHolder ?: return
                val next = if (position + 1 < itemCount) getItem(position + 1) else null
                h.bind(viewModel, item, isLast = next == null || next.viewType != TYPE_ROW)
            }
            TYPE_AD -> {
                val h = holder as? TasksNativeAdHolder ?: return
                releasable.add(h.loadAd())
            }
            TYPE_EMPTY -> {
                val h = holder as? TasksEmptyHolder ?: return
                h.bind(item)
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is TasksNativeAdHolder) {
            holder.release()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    companion object {
        const val TYPE_ROW = 0
        const val TYPE_AD = 1
        const val TYPE_EMPTY = 2

        fun makeItems(
            list: List<TaskDetail>,
            filterType: TasksFilterType?,
            isPremium: Boolean = false
        ): List<TasksItem> {
            return TasksListBuilder.build(list, filterType ?: TasksFilterType.ALL_TASKS, isPremium)
        }
    }

    data class TasksItem(
        val viewType: Int,
        val taskDetail: TaskDetail?,
        val isPremium: Boolean,
        val stringRes: Int? = null,
        val state: TaskState? = null
    )
}

class TasksDiffCallback : DiffUtil.ItemCallback<TasksAdapter.TasksItem>() {

    override fun areItemsTheSame(
        oldItem: TasksAdapter.TasksItem,
        newItem: TasksAdapter.TasksItem
    ): Boolean {
        return oldItem.viewType == newItem.viewType &&
                oldItem.taskDetail?.task?.id == newItem.taskDetail?.task?.id &&
                oldItem.stringRes == newItem.stringRes
    }

    override fun areContentsTheSame(
        oldItem: TasksAdapter.TasksItem,
        newItem: TasksAdapter.TasksItem
    ): Boolean {
        return oldItem.taskDetail?.task == newItem.taskDetail?.task &&
                oldItem.taskDetail?.account == newItem.taskDetail?.account &&
                oldItem.state == newItem.state &&
                oldItem.isPremium == newItem.isPremium
    }
}
