package dev.kxxcn.maru.view.tasks

import dev.kxxcn.maru.view.custom.SplitBarView
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.util.extension.px
import dev.kxxcn.maru.util.preference.PreferenceUtils

@BindingAdapter("app:tasks")
fun setTasks(view: RecyclerView, items: List<TasksAdapter.TasksItem>?) {
    items?.let {
        with(view) {
            (adapter as? TasksAdapter)?.submitList(it)
            if (PreferenceUtils.forceScroll) {
                postDelayed({ smoothScrollToPosition(0) }, 200)
            }
        }
    }
}

@BindingAdapter("app:resourceId")
fun setResourceId(view: ImageView, resourceId: String?) {
    val context = view.context
    val packageName = context.packageName
    val resource = context.resources
    val drawable = if (resourceId == null) {
        R.drawable.ic_empty
    } else {
        resource.getIdentifier(resourceId, "drawable", packageName)
    }
    view.setImageResource(drawable)
}

@BindingAdapter("app:groomRatio", "app:brideRatio")
fun setSplitProgress(view: SplitBarView, groomRatio: Float, brideRatio: Float) {
    view.setProgress(groomRatio, brideRatio)
}
