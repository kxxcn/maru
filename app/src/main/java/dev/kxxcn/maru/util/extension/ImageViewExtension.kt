package dev.kxxcn.maru.util.extension

import android.widget.ImageView
import androidx.annotation.ColorInt
import dev.kxxcn.maru.GlideApp

fun <T> ImageView.load(t: T) {
    GlideApp.with(this).load(t).into(this)
}

fun <T> ImageView.loadCircleCrop(t: T) {
    GlideApp.with(this).load(t).circleCrop().into(this)
}

fun ImageView.setTint(@ColorInt color: Int) {
    setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
}
