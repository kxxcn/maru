package dev.kxxcn.maru.view.notice

import android.util.SparseBooleanArray
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.data.Notice
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.notice.NoticeAdapter.NoticeItem

class NoticeAdapter :
    ListAdapter<NoticeItem, RecyclerView.ViewHolder>(NoticeDiffCallback()) {

    private val releasable = mutableListOf<() -> Unit>()

    private val expandable = SparseBooleanArray()

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

    private fun release() {
        releasable.forEach { release -> release() }
        releasable.clear()
        expandable.clear()
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
