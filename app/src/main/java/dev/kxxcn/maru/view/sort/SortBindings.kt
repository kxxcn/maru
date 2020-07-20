package dev.kxxcn.maru.view.sort

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.data.Task

@BindingAdapter("app:sortTasks")
fun setSortTasks(view: RecyclerView, items: List<Task?>?) {
    items?.let {
        with(view) {
            (adapter as? SortAdapter)?.submitList(it)
        }
    }
}
