package dev.kxxcn.maru.view.more.contents

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.databinding.MoreContentsItemBinding
import dev.kxxcn.maru.util.CONTENTS_ITEM_SPAN_COUNT
import dev.kxxcn.maru.util.GridSpacingDecoration
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.more.MoreViewModel

class MoreContentsHolder(
    private val binding: MoreContentsItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(viewModel: MoreViewModel): () -> Unit {
        with(binding) {
            with(contentsList) {
                addItemDecoration(GridSpacingDecoration(spanCount = CONTENTS_ITEM_SPAN_COUNT), 0)
                adapter = MoreContentsAdapter(viewModel)
            }
            this.lifecycleOwner = this@MoreContentsHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
        return { release() }
    }

    private fun release() {
        binding.contentsList.adapter = null
    }

    companion object {

        fun from(parent: ViewGroup): MoreContentsHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = MoreContentsItemBinding.inflate(inflater, parent, false)
            return MoreContentsHolder(binding)
        }
    }
}
