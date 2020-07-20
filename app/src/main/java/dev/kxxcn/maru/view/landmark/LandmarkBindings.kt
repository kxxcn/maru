package dev.kxxcn.maru.view.landmark

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import dev.kxxcn.maru.GlideApp
import dev.kxxcn.maru.R
import java.text.NumberFormat
import java.util.*

@BindingAdapter("app:landmarkImage")
fun setLandmarkImage(view: ImageView, imageRes: Int) {
    GlideApp.with(view)
        .load(imageRes)
        .centerCrop()
        .into(view)
}

@BindingAdapter("app:unitText")
fun setUnitText(view: TextView, text: String) {
    val end = text.indexOf(" ")
    SpannableStringBuilder(text).apply {
        setSpan(
            RelativeSizeSpan(2f),
            0,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }.also {
        view.text = it
    }
}

@BindingAdapter("app:priceText")
fun setPriceText(view: TextView, price: Int) {
    val context = view.context ?: return
    view.text = try {
        val numberFormat = NumberFormat.getInstance(Locale.KOREA)
        context.getString(R.string.landmark_fare_format, numberFormat.format(price))
    } catch (e: Exception) {
        context.getString(R.string.landmark_fare_not_found)
    }
}
