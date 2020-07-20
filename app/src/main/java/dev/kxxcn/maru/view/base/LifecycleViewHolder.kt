package dev.kxxcn.maru.view.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView

abstract class LifecycleViewHolder(
    binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    fun onAttach() {
        with(lifecycleRegistry) {
            currentState = Lifecycle.State.CREATED
            currentState = Lifecycle.State.STARTED
        }
    }

    fun onDetach() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }
}
