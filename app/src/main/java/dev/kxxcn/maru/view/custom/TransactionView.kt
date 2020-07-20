package dev.kxxcn.maru.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.databinding.TransactionViewBinding

class TransactionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: TransactionViewBinding?

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.transaction_view,
            this,
            true
        )
    }

    fun bind(task: TaskDetail) {
        binding?.item = task
    }

    fun release() {
        binding = null
    }
}
