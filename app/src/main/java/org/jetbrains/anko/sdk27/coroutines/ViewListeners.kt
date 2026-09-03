package org.jetbrains.anko.sdk27.coroutines

import android.view.MotionEvent
import android.view.View

fun View.onClick(listener: (View) -> Unit) {
    setOnClickListener { listener(it) }
}

fun View.onLongClick(listener: (View) -> Unit) {
    setOnLongClickListener {
        listener(it)
        true
    }
}

fun View.onTouch(listener: (View, MotionEvent) -> Unit) {
    setOnTouchListener { view, event ->
        listener(view, event)
        false
    }
}
