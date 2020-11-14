package dev.kxxcn.maru.view.tasks

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.util.AD_INTERVAL
import dev.kxxcn.maru.view.base.LifecycleAdapter
import dev.kxxcn.maru.view.tasks.holder.*

class TasksAdapter(
    private val viewModel: TasksViewModel,
    private val requestManager: RequestManager
) : LifecycleAdapter<TasksAdapter.TasksItem, RecyclerView.ViewHolder>(TasksDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PREPARE -> TasksPrepareHolder.from(parent)
            TYPE_COMPLETED -> TasksCompletedHolder.from(parent)
            TYPE_ACTIVE -> TasksActiveHolder.from(parent)
            TYPE_AD -> TasksNativeAdHolder.from(parent, requestManager)
            else -> TasksEmptyHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.viewType) {
            TYPE_PREPARE -> {
                val h = holder as? TasksPrepareHolder ?: return
                releasable.add(h.bind(viewModel, item.taskDetail, item.isPremium))
            }
            TYPE_COMPLETED -> {
                val h = holder as? TasksCompletedHolder ?: return
                h.bind(viewModel, item.taskDetail, item.isPremium)
            }
            TYPE_ACTIVE -> {
                val h = holder as? TasksActiveHolder ?: return
                h.bind(viewModel, item.taskDetail, item.isPremium)
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

        private const val TYPE_PREPARE = 0

        private const val TYPE_COMPLETED = 1

        private const val TYPE_ACTIVE = 2

        private const val TYPE_AD = 3

        private const val TYPE_EMPTY = 4

        fun makeItems(
            list: List<TaskDetail>,
            filterType: TasksFilterType?,
            isPremium: Boolean = false
        ): List<TasksItem> {
            val items = mutableListOf<TasksItem>()
            if (list.isEmpty()) {
                when (filterType) {
                    TasksFilterType.ACTIVE_TASKS -> R.string.task_empty_progress
                    else -> R.string.task_empty_completed
                }.let {
                    items.add(TasksItem(TYPE_EMPTY, null, isPremium, it))
                }
            } else {
                for ((index, taskDetail) in list.withIndex()) {
                    when (filterType) {
                        null -> {
                            return emptyList()
                        }
                        TasksFilterType.COMPLETED_TASKS -> {
                            items.add(
                                TasksItem(
                                    TYPE_COMPLETED,
                                    taskDetail,
                                    isPremium
                                )
                            )
                            if (!isPremium && index % AD_INTERVAL == 0) {
                                items.add(
                                    TasksItem(
                                        TYPE_AD,
                                        TaskDetail(),
                                        isPremium
                                    )
                                )
                            }
                        }
                        else -> {
                            if (taskDetail.task?.isCompleted == true) {
                                items.add(
                                    TasksItem(
                                        TYPE_COMPLETED,
                                        taskDetail,
                                        isPremium = isPremium
                                    )
                                )
                            } else {
                                if (taskDetail.account?.remain == null || taskDetail.account?.remain == 0L) {
                                    items.add(
                                        TasksItem(
                                            TYPE_PREPARE,
                                            taskDetail,
                                            isPremium
                                        )
                                    )
                                } else {
                                    items.add(
                                        TasksItem(
                                            TYPE_ACTIVE,
                                            taskDetail,
                                            isPremium
                                        )
                                    )
                                }
                            }
                            if (!isPremium && index % AD_INTERVAL == 0) {
                                items.add(
                                    TasksItem(
                                        TYPE_AD,
                                        TaskDetail(),
                                        isPremium
                                    )
                                )
                            }
                        }
                    }
                }
            }
            return items
        }
    }

    data class TasksItem(
        val viewType: Int,
        val taskDetail: TaskDetail?,
        val isPremium: Boolean,
        val stringRes: Int? = null
    )
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
