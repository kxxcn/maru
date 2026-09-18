package dev.kxxcn.maru.view.tasks.holder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.databinding.TaskRowItemBinding
import dev.kxxcn.maru.util.ConvertUtils
import dev.kxxcn.maru.util.DateUtils
import dev.kxxcn.maru.view.tasks.TaskState
import dev.kxxcn.maru.view.tasks.TasksAdapter
import dev.kxxcn.maru.view.tasks.TasksViewModel
import dev.kxxcn.maru.view.tasks.setResourceId

/**
 * 체크리스트 단일 행. 상태(준비 전, 진행 중, 완료)는 체크 원, 타일 색, 메타 문구로만 구분한다.
 */
class TasksRowHolder(
    private val binding: TaskRowItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: TasksViewModel, item: TasksAdapter.TasksItem, isLast: Boolean) {
        val detail = item.taskDetail ?: return
        val state = item.state ?: TaskState.READY
        val context = binding.root.context
        with(binding) {
            taskName.text = detail.task?.name
            taskName.setTextColor(
                ContextCompat.getColor(context, if (state == TaskState.DONE) R.color.maru_ink_2 else R.color.maru_ink)
            )
            setResourceId(taskTile, detail.task?.iconId)
            taskTile.setBackgroundResource(
                when (state) {
                    TaskState.ACTIVE -> R.drawable.bg_tile_active
                    TaskState.DONE -> R.drawable.bg_tile_done
                    TaskState.READY -> R.drawable.bg_tile_ready
                }
            )
            taskCheck.isSelected = state == TaskState.DONE
            taskCheck.contentDescription = context.getString(
                if (state == TaskState.DONE) R.string.tasks_row_undo_complete else R.string.tasks_row_complete
            )
            taskMeta.text = metaText(context, detail, state)
            taskSplit.isVisible = state == TaskState.ACTIVE && detail.totalAccounts > 0L
            if (taskSplit.isVisible) {
                taskSplit.setProgress(detail.husbandProgress, detail.wifeProgress)
            }
            taskDivider.isVisible = !isLast
            taskRow.setOnClickListener { viewModel.select(detail, item.isPremium) }
            taskCheck.setOnClickListener { viewModel.toggleComplete(detail) }
        }
    }

    private fun metaText(context: Context, detail: TaskDetail, state: TaskState): String {
        val account = detail.account
        val total = ConvertUtils.moneyText(detail.totalAccounts)
        return when (state) {
            TaskState.READY -> context.getString(R.string.tasks_row_ready_hint)
            TaskState.ACTIVE -> {
                val remain = account?.remain ?: 0L
                val remainText = if (remain > 0L) {
                    context.getString(R.string.tasks_row_remain, ConvertUtils.moneyText(remain))
                } else {
                    context.getString(R.string.tasks_row_no_remain)
                }
                "$total   $remainText"
            }
            TaskState.DONE -> {
                if (account == null) {
                    context.getString(R.string.tasks_row_skipped)
                } else {
                    val date = DateUtils.DATE_FORMAT_6.format(account.date)
                    "$total   " + context.getString(R.string.tasks_row_done_date, date)
                }
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): TasksRowHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TaskRowItemBinding.inflate(inflater, parent, false)
            return TasksRowHolder(binding)
        }
    }
}
