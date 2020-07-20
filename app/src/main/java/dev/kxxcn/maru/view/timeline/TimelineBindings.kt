package dev.kxxcn.maru.view.timeline

import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.view.custom.TimelineView

@BindingAdapter("app:timelineItems")
fun setTimelineItems(view: RecyclerView, items: List<TimelineItem>?) {
    items?.let {
        with(view) {
            (adapter as? TimelineAdapter)?.submitList(it)
        }
    }
}

@BindingAdapter("app:timelineView")
fun setTimelineView(view: LinearLayout, items: List<TimelineTask>?) {
    view.removeAllViews()
    items?.let {
        for (task in it) {
            TimelineView(view.context).apply {
                bind(task)
            }.also { v ->
                view.addView(v)
            }
        }
    }
}
