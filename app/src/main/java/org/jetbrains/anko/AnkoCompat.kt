package org.jetbrains.anko

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.text.SpannableStringBuilder
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

val matchParent: Int = ViewGroup.LayoutParams.MATCH_PARENT

val wrapContent: Int = ViewGroup.LayoutParams.WRAP_CONTENT

var TextView.textColor: Int
    get() = currentTextColor
    set(value) {
        setTextColor(value)
    }

var ImageView.imageResource: Int
    get() = 0
    set(value) {
        setImageResource(value)
    }

val Context.notificationManager: NotificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

fun Intent.singleTop(): Intent = addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

fun buildSpanned(builderAction: SpannableStringBuilder.() -> Unit): SpannableStringBuilder {
    return SpannableStringBuilder().apply(builderAction)
}
