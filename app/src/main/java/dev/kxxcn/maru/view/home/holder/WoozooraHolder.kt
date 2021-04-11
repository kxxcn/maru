package dev.kxxcn.maru.view.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.databinding.WoozooraItemBinding
import dev.kxxcn.maru.view.base.BaseViewModel
import dev.kxxcn.maru.view.base.LifecycleViewHolder

class WoozooraHolder(
    private val binding: WoozooraItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(viewModel: BaseViewModel) {
        with(binding) {
            this.lifecycleOwner = this@WoozooraHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): WoozooraHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = WoozooraItemBinding.inflate(inflater, parent, false)
            return WoozooraHolder(binding)
        }
    }
}
