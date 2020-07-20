package dev.kxxcn.maru.view.notice

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.data.Notice
import dev.kxxcn.maru.view.notice.NoticeAdapter.NoticeItem

@BindingAdapter("app:notices")
fun setNotices(view: RecyclerView, items: List<Notice>?) {
    if (items == null || items.isEmpty()) {
        listOf(NoticeItem(NoticeAdapter.TYPE_EMPTY, null))
    } else {
        items.map { NoticeItem(NoticeAdapter.TYPE_VIEW, it) }
    }.also {
        (view.adapter as? NoticeAdapter)?.submitList(it)
    }
}
