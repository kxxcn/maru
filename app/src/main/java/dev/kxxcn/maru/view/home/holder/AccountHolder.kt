package dev.kxxcn.maru.view.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.databinding.AccountItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.home.HomeAdapter

class AccountHolder(
    private val binding: AccountItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(item: HomeAdapter.SummaryItem) {
        with(binding) {
            this.lifecycleOwner = this@AccountHolder
            this.content = item.content
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): AccountHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = AccountItemBinding.inflate(inflater, parent, false)
            return AccountHolder(binding)
        }
    }
}
