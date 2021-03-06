package dev.kxxcn.maru.view.home.holder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.AccountItemBinding
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.home.HomeAdapter

class AccountHolder(
    private val binding: AccountItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(item: HomeAdapter.SummaryItem) {
        val color = if (PreferenceUtils.useDarkMode) {
            ContextCompat.getColor(binding.root.context, R.color.colorPrimaryDarkNight)
        } else {
            Color.WHITE
        }
        with(binding) {
            this.lifecycleOwner = this@AccountHolder
            this.content = item.content
            this.totalAccountsChart.setHoleColor(color)
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
