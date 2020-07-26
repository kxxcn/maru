package dev.kxxcn.maru.view.base

import android.util.SparseBooleanArray
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class LifecycleAdapter<T, VH : RecyclerView.ViewHolder>(
    callback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(callback) {

    val releasable = mutableListOf<() -> Unit>()

    val expandable = SparseBooleanArray()

    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        (holder as? LifecycleViewHolder)?.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: VH) {
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
        expandable.clear()
    }
}
