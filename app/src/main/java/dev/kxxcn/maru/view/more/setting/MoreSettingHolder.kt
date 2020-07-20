package dev.kxxcn.maru.view.more.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.maru.databinding.MoreSettingItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.more.MoreViewModel

class MoreSettingHolder(
    private val binding: MoreSettingItemBinding
) : LifecycleViewHolder(binding) {

    fun bind(viewModel: MoreViewModel): () -> Unit {
        with(binding) {
            this.lifecycleOwner = this@MoreSettingHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
        return { release() }
    }

    fun release() {
        with(binding) {
            settingsIcon.setOnClickListener(null)
            contactIcon.setOnClickListener(null)
            noticeIcon.setOnClickListener(null)
            settingsIcon.release()
            contactIcon.release()
            noticeIcon.release()
        }
    }

    companion object {

        fun from(parent: ViewGroup): MoreSettingHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = MoreSettingItemBinding.inflate(inflater, parent, false)
            return MoreSettingHolder(binding)
        }
    }
}
