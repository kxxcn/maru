package dev.kxxcn.maru.view.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import dev.kxxcn.maru.BR
import dev.kxxcn.maru.data.Summary
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.home.HomeViewModel

/**
 * 홈의 블록 하나. 레이아웃은 모두 content(Summary)와 viewModel 두 변수만 쓴다.
 */
class HomeBindingHolder(
    private val binding: ViewDataBinding
) : LifecycleViewHolder(binding) {

    fun bind(content: Summary, viewModel: HomeViewModel) {
        binding.lifecycleOwner = this
        binding.setVariable(BR.content, content)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup, @LayoutRes layoutRes: Int): HomeBindingHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutRes, parent, false)
            return HomeBindingHolder(binding)
        }
    }
}
