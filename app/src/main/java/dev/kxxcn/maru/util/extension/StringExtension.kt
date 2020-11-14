package dev.kxxcn.maru.util.extension

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Base64
import dev.kxxcn.maru.BuildConfig

fun String?.moneyToLong() = this?.replace(",", "")?.toLong() ?: 0L

infix fun String.or(debug: String) = if (BuildConfig.DEBUG) debug else this

fun String.coordinates(): Pair<Double, Double> {
    val coordinates = this.split(",")
    val longitude = coordinates[0].trim().toDouble()
    val latitude = coordinates[1].trim().toDouble()
    return longitude to latitude
}

fun String.encode(): String = Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)

fun String.decode(): String = String(Base64.decode(this, Base64.NO_WRAP))

fun SpannableStringBuilder.bold(
    start: Int,
    end: Int,
    builderAction: SpannableStringBuilder.() -> Unit
): SpannableStringBuilder {
    builderAction()
    setSpan(
        StyleSpan(Typeface.BOLD),
        start,
        end,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}
