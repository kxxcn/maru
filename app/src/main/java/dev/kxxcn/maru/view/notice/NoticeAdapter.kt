package dev.kxxcn.maru.view.notice

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.data.Notice
import dev.kxxcn.maru.view.base.LifecycleAdapter
import dev.kxxcn.maru.view.notice.NoticeAdapter.NoticeItem

class NoticeAdapter : LifecycleAdapter<NoticeItem, RecyclerView.ViewHolder>(NoticeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY -> NoticeEmptyViewHolder.from(parent)
            else -> NoticeViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NoticeViewHolder) {
            val notice = getItem(position).notice ?: return
            releasable.add(holder.bind(notice, isExpand(position)))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onCurrentListChanged(
        previousList: MutableList<NoticeItem>,
        currentList: MutableList<NoticeItem>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        expandable.clear()
    }

    private fun isExpand(pos: Int): Boolean {
        return expandable[pos]
    }

    fun put(pos: Int, isExpand: Boolean) {
        expandable.put(pos, isExpand)
    }

    companion object {

        const val TYPE_EMPTY = 0

        const val TYPE_VIEW = 1
    }

    data class NoticeItem(val type: Int, val notice: Notice?)
}

class NoticeDiffCallback : DiffUtil.ItemCallback<NoticeItem>() {

    override fun areItemsTheSame(
        oldItem: NoticeItem,
        newItem: NoticeItem
    ): Boolean {
        return oldItem.notice?.subject == newItem.notice?.subject
    }

    override fun areContentsTheSame(
        oldItem: NoticeItem,
        newItem: NoticeItem
    ): Boolean {
        return oldItem == newItem
    }
}
