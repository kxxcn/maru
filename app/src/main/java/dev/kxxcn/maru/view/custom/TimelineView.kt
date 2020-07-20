package dev.kxxcn.maru.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.TimelineViewBinding
import dev.kxxcn.maru.view.timeline.TimelineTask

class TimelineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: TimelineViewBinding? = null

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.timeline_view,
            this,
            true
        )
    }

    fun bind(timelineTask: TimelineTask) {
        binding?.item = timelineTask
    }

    fun release() {
        binding = null
    }
}
