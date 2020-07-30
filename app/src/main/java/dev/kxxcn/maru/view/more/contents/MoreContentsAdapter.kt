package dev.kxxcn.maru.view.more.contents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.databinding.ContentsItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.more.MoreViewModel
import dev.kxxcn.maru.view.more.contents.ContentsItem.*

class MoreContentsAdapter(
    private val viewModel: MoreViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val releasable = mutableListOf<() -> Unit>()

    private var items: List<ContentsItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContentsHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items?.get(position) ?: return
        val h = holder as? ContentsHolder ?: return
        releasable.add(h.bind(viewModel, item))
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

    fun updateItems(items: List<ContentsItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    private fun release() {
        releasable.forEach { release -> release() }
        releasable.clear()
    }

    companion object {

        fun makeItems() = listOf(
            AD,
            BACKUP,
            NIGHT,
            REVIEW,
            DAYS,
            TIMELINE,
            ORDER,
            RING,
            DRESS,
            TUXEDO,
            HANBOK,
            LANDMARK
        )
    }

    class ContentsHolder(
        private val binding: ContentsItemBinding
    ) : LifecycleViewHolder(binding) {

        fun bind(viewModel: MoreViewModel, item: ContentsItem): () -> Unit {
            with(binding) {
                this.lifecycleOwner = this@ContentsHolder
                this.viewModel = viewModel
                this.item = item
                this.executePendingBindings()
            }
            return { release() }
        }

        private fun release() {
            binding.contentsText.release()
        }

        companion object {

            fun from(parent: ViewGroup): ContentsHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ContentsItemBinding.inflate(layoutInflater, parent, false)
                return ContentsHolder(binding)
            }
        }
    }
}
