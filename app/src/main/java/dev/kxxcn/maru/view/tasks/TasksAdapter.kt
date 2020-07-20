package dev.kxxcn.maru.view.tasks

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.util.AD_INTERVAL
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.tasks.holder.*

class TasksAdapter(
    private val viewModel: TasksViewModel
) : ListAdapter<TasksAdapter.TasksItem, RecyclerView.ViewHolder>(TasksDiffCallback()) {

    private val releasable = mutableListOf<() -> Unit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PREPARE -> TasksPrepareHolder.from(parent)
            TYPE_COMPLETED -> TasksCompletedHolder.from(parent)
            TYPE_ACTIVE -> TasksActiveHolder.from(parent)
            TYPE_AD -> TasksNativeAdHolder.from(parent)
            else -> TasksEmptyHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.viewType) {
            TYPE_PREPARE -> {
                val h = holder as? TasksPrepareHolder ?: return
                releasable.add(h.bind(viewModel, item.taskDetail))
            }
            TYPE_COMPLETED -> {
                val h = holder as? TasksCompletedHolder ?: return
                h.bind(viewModel, item.taskDetail)
            }
            TYPE_ACTIVE -> {
                val h = holder as? TasksActiveHolder ?: return
                h.bind(viewModel, item.taskDetail)
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

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder as? LifecycleViewHolder)?.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as? LifecycleViewHolder)?.onDetach()
        super.onViewDetachedFromWindow(holder)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        release()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    private fun release() {
        releasable.forEach { release -> release() }
        releasable.clear()
    }

    companion object {

        private const val TYPE_PREPARE = 0

        private const val TYPE_COMPLETED = 1

        private const val TYPE_ACTIVE = 2

        private const val TYPE_AD = 3

        private const val TYPE_EMPTY = 4

        fun makeItems(list: List<TaskDetail>, filterType: TasksFilterType?): List<TasksItem> {
            val items = mutableListOf<TasksItem>()
            if (list.isEmpty()) {
                when (filterType) {
                    TasksFilterType.ACTIVE_TASKS -> R.string.task_empty_progress
                    else -> R.string.task_empty_completed
                }.let {
                    items.add(TasksItem(TYPE_EMPTY, null, it))
                }
            } else {
                for ((index, taskDetail) in list.withIndex()) {
                    when (filterType) {
                        null -> {
                            return emptyList()
                        }
                        TasksFilterType.COMPLETED_TASKS -> {
                            items.add(TasksItem(TYPE_COMPLETED, taskDetail))
                            if (index % AD_INTERVAL == 0) items.add(
                                TasksItem(
                                    TYPE_AD,
                                    TaskDetail()
                                )
                            )
                        }
                        else -> {
                            if (taskDetail.task?.isCompleted == true) {
                                items.add(TasksItem(TYPE_COMPLETED, taskDetail))
                            } else {
                                if (taskDetail.account?.remain == null || taskDetail.account?.remain == 0L) {
                                    items.add(TasksItem(TYPE_PREPARE, taskDetail))
                                } else {
                                    items.add(TasksItem(TYPE_ACTIVE, taskDetail))
                                }
                            }
                            if (index % AD_INTERVAL == 0) items.add(
                                TasksItem(
                                    TYPE_AD,
                                    TaskDetail()
                                )
                            )
                        }
                    }
                }
            }
            return items
        }
    }

    data class TasksItem(val viewType: Int, val taskDetail: TaskDetail?, val stringRes: Int? = null)
}

class TasksDiffCallback : DiffUtil.ItemCallback<TasksAdapter.TasksItem>() {

    override fun areItemsTheSame(
        oldItem: TasksAdapter.TasksItem,
        newItem: TasksAdapter.TasksItem
    ): Boolean {
        return oldItem.taskDetail?.task == newItem.taskDetail?.task &&
                oldItem.taskDetail?.account == newItem.taskDetail?.account
    }

    override fun areContentsTheSame(
        oldItem: TasksAdapter.TasksItem,
        newItem: TasksAdapter.TasksItem
    ): Boolean {
        return oldItem.taskDetail == newItem.taskDetail
    }
}
