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
fun setUnitText(view: TextView, text: String?) {
    if (text.isNullOrBlank() || text.contains("null", ignoreCase = true)) {
        view.setText(R.string.landmark_value_not_found)
        return
    }

    val end = text.indexOf(" ")
    if (end <= 0) {
        view.text = text
        return
    }

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

@BindingAdapter("app:distanceText")
fun setDistanceText(view: TextView, distance: Int?) {
    setUnitText(
        view,
        distance?.let { view.context.getString(R.string.landmark_distance, it) }
    )
}

@BindingAdapter("app:timeText")
fun setTimeText(view: TextView, time: Long?) {
    setUnitText(
        view,
        time?.let { view.context.getString(R.string.landmark_time, it) }
    )
}

@BindingAdapter("app:priceText")
fun setPriceText(view: TextView, price: Int?) {
    view.text = if (price == null) {
        view.context.getString(R.string.landmark_value_not_found)
    } else {
        try {
            val numberFormat = NumberFormat.getInstance(Locale.KOREA)
            view.context.getString(R.string.landmark_fare_format, numberFormat.format(price))
        } catch (e: Exception) {
            view.context.getString(R.string.landmark_fare_not_found)
        }
    }
}
