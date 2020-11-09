package dev.kxxcn.maru.view.tasks

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.util.PieChartFormatter
import dev.kxxcn.maru.util.extension.px
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.custom.RotateSelectionView

@BindingAdapter("app:selection")
fun setSelectionText(view: RotateSelectionView, isSelect: Boolean) {
    view.handleFilterSelection(isSelect)
}

@BindingAdapter("app:tasks")
fun setTasks(view: RecyclerView, items: List<TasksAdapter.TasksItem>?) {
    items?.let {
        with(view) {
            (adapter as? TasksAdapter)?.submitList(it)
            if (PreferenceUtils.forceScroll) {
                postDelayed({ smoothScrollToPosition(0) }, 200)
            }
        }
    }
}

@BindingAdapter("app:resourceId")
fun setResourceId(view: ImageView, resourceId: String?) {
    val context = view.context
    val packageName = context.packageName
    val resource = context.resources
    val drawable = if (resourceId == null) {
        R.drawable.ic_empty
    } else {
        resource.getIdentifier(resourceId, "drawable", packageName)
    }
    view.setImageResource(drawable)
}

@BindingAdapter("app:chart")
fun setPieChart(view: PieChart, taskDetail: TaskDetail) {
    with(view) {
        setUsePercentValues(true)
        description.isEnabled = false
        dragDecelerationFrictionCoef = 0.95f
        setDrawSlicesUnderHole(false)
        setDrawCenterText(false)
        isHighlightPerTapEnabled = false
        isRotationEnabled = false

        with(legend) {
            isEnabled = false
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            orientation = Legend.LegendOrientation.VERTICAL
            setDrawInside(false)
        }

        mutableListOf<PieEntry>().apply {
            add(0, PieEntry(taskDetail.husbandProgress))
            add(1, PieEntry(taskDetail.wifeProgress))
        }.run {
            PieDataSet(this, null).apply {
                sliceSpace = 5f
                selectionShift = 5f
                setColors(
                    ContextCompat.getColor(view.context, R.color.progressBarForeground),
                    ContextCompat.getColor(view.context, R.color.progressBarSecondaryForeground)
                )
            }
        }.run {
            PieData(this).apply {
                setValueFormatter(PieChartFormatter(this@with))
                setValueTextColor(ContextCompat.getColor(view.context, android.R.color.white))
                setValueTextSize(4.px.toFloat())
            }
        }.also {
            data = it
        }

        if (taskDetail.husbandProgress == taskDetail.wifeProgress || taskDetail.husbandProgress == 0f || taskDetail.wifeProgress == 0f) {
            highlightValues(null)
        } else {
            val x = if (taskDetail.husbandProgress > taskDetail.wifeProgress) {
                0f
            } else {
                1f
            }
            highlightValue(x, 0)
        }
        invalidate()
    }
}
