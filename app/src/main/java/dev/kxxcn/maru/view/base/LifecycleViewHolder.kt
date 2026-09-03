package dev.kxxcn.maru.view.base

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView

abstract class LifecycleViewHolder(
        private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    private var lifecycleRegistry = LifecycleRegistry(this)

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    override val lifecycle: Lifecycle
        get() {
            ensureReusableLifecycle()
            return lifecycleRegistry
        }

    fun onAttach() {
        ensureReusableLifecycle()
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    fun onDetach() {
        if (lifecycleRegistry.currentState != Lifecycle.State.DESTROYED) {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }
    }

    fun onDestroy() {
        if (lifecycleRegistry.currentState != Lifecycle.State.DESTROYED) {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }
        binding.lifecycleOwner = null
    }

    private fun ensureReusableLifecycle() {
        if (lifecycleRegistry.currentState == Lifecycle.State.DESTROYED) {
            lifecycleRegistry = LifecycleRegistry(this).apply {
                currentState = Lifecycle.State.INITIALIZED
            }
        }
    }
}
