package dev.kxxcn.maru.util.extension

import android.content.res.Resources

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.dpToPx: Float
    get() {
        val scale: Float = Resources.getSystem().displayMetrics.density
        return this * scale + 0.5f
    }

val Float.spToPx: Float
    get() {
        val scale: Float = Resources.getSystem().displayMetrics.scaledDensity
        return this * scale
    }

val Int?.km: Int
    get() = if (this == null) 0 else this / 1000
