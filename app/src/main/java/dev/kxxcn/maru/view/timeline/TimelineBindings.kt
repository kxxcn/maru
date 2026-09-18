package dev.kxxcn.maru.view.timeline

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.R

@BindingAdapter("app:timelineItems")
fun setTimelineItems(view: RecyclerView, items: List<TimelineRow>?) {
    items?.let { (view.adapter as? TimelineAdapter)?.submitList(it) }
}

@BindingAdapter("app:timelineDot")
fun setTimelineDot(view: View, state: TimelineState?) {
    view.setBackgroundResource(
        when (state) {
            TimelineState.PAST -> R.drawable.dot_timeline_past
            TimelineState.NOW -> R.drawable.dot_timeline_now
            else -> R.drawable.dot_timeline_future
        }
    )
}

/**
 * 시기 묶음 안의 할 일들. 탭하면 가이드 문구를 펼치고 접는다. 지난 시기는 체크 표시.
 */
@BindingAdapter("app:timelineTasks", "app:timelineState")
fun setTimelineTasks(view: LinearLayout, tasks: List<TimelineTask>?, state: TimelineState?) {
    view.removeAllViews()
    val context = view.context
    val inflater = LayoutInflater.from(context)
    tasks.orEmpty().forEach { task ->
        val itemView = inflater.inflate(R.layout.timeline_task_item, view, false)
        val icon = itemView.findViewById<ImageView>(R.id.timeline_task_icon)
        val name = itemView.findViewById<TextView>(R.id.timeline_task_name)
        val content = itemView.findViewById<TextView>(R.id.timeline_task_content)
        name.text = context.getString(task.nameRes)
        content.text = context.getString(task.contentRes)
        val past = state == TimelineState.PAST
        icon.setImageResource(if (past) R.drawable.ic_check_mark else R.drawable.ic_arrow_right)
        icon.imageTintList = ContextCompat.getColorStateList(context, if (past) R.color.maru_done else R.color.maru_muted)
        icon.rotation = 0f
        name.setTextColor(ContextCompat.getColor(context, if (past) R.color.maru_muted else R.color.maru_ink_2))
        itemView.setOnClickListener {
            val open = !content.isVisible
            content.isVisible = open
            if (!past) {
                icon.rotation = if (open) 90f else 0f
                name.setTextColor(ContextCompat.getColor(context, if (open) R.color.maru_ink else R.color.maru_ink_2))
            }
        }
        view.addView(itemView)
    }
}
