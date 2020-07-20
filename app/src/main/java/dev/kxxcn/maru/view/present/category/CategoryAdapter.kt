package dev.kxxcn.maru.view.present.category

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.kxxcn.maru.data.Present
import dev.kxxcn.maru.view.present.PresentViewModel

class PresentAdapter(
    private val viewModel: PresentViewModel,
    private val size: Int
) : ListAdapter<Present, CategoryViewHolder>(PresentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent, size)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    override fun onViewRecycled(holder: CategoryViewHolder) {
        super.onViewRecycled(holder)
        holder.clear()
    }

    override fun onViewAttachedToWindow(holder: CategoryViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: CategoryViewHolder) {
        holder.onDetach()
        super.onViewDetachedFromWindow(holder)
    }
}

class PresentDiffCallback : DiffUtil.ItemCallback<Present>() {

    override fun areItemsTheSame(oldItem: Present, newItem: Present): Boolean {
        return oldItem.titleRes == newItem.titleRes
    }

    override fun areContentsTheSame(oldItem: Present, newItem: Present): Boolean {
        return oldItem == newItem
    }
}
