package dev.kxxcn.maru.view.status

import androidx.databinding.BindingAdapter
import dev.kxxcn.maru.view.custom.ArcProgressView

@BindingAdapter("app:arcProgress")
fun setArcProgress(view: ArcProgressView, progress: Double) {
    view.setProgress(progress.toFloat())
}