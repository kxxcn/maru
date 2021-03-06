package dev.kxxcn.maru.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.databinding.RemainTransactionViewBinding

class RemainTransactionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), LifecycleOwner {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    private var binding: RemainTransactionViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.remain_transaction_view,
            this,
            true
        )

        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    fun bind(taskDetail: TaskDetail) {
        binding.lifecycleOwner = this
        binding.item = taskDetail
        binding.executePendingBindings()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }
}
