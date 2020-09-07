package dev.kxxcn.maru.view.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.databinding.WelcomeItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.home.HomeAdapter
import dev.kxxcn.maru.view.home.HomeViewModel

class WelcomeHolder(
    private val binding: WelcomeItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(item: HomeAdapter.SummaryItem, viewModel: HomeViewModel) {
        with(binding) {
            this.lifecycleOwner = this@WelcomeHolder
            this.viewModel = viewModel
            this.content = item.content
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): WelcomeHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = WelcomeItemBinding.inflate(layoutInflater, parent, false)
            return WelcomeHolder(binding)
        }
    }
}
