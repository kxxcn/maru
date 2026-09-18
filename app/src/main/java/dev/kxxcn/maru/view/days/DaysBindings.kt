package dev.kxxcn.maru.view.days

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Day
import dev.kxxcn.maru.util.ConvertUtils
import java.text.NumberFormat
import java.util.Locale

@BindingAdapter("app:daysList")
fun setDays(view: RecyclerView, days: List<Day>?) {
    days?.let {
        (view.adapter as? DaysAdapter)?.submitList(it)
    }
}

@BindingAdapter("app:selected")
fun setSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}

/** "D-38", "D+130", "D-Day", "1,286일" 형태의 표시 문자열. */
fun dayNumberText(context: Context, day: Day): String {
    val (res, count) = ConvertUtils.getDaysCount(day.date, day.type)
    return if (res == R.string.days_add_calculate_count) {
        context.getString(R.string.days_display_count, NumberFormat.getInstance(Locale.KOREA).format(count))
    } else {
        context.getString(res, count)
    }
}

/** 지난 디데이(D+)인지. 날짜수 타입은 지난 날이 아니다. */
fun isPastDay(day: Day): Boolean {
    val (_, count) = ConvertUtils.getDaysCount(day.date, day.type)
    return day.type == DaysFilterType.REMAIN && count > 0
}

@BindingAdapter("app:days")
fun setDays(view: TextView, day: Day) {
    view.text = dayNumberText(view.context, day)
    view.setTextColor(
        ContextCompat.getColor(view.context, if (isPastDay(day)) R.color.maru_muted else R.color.maru_ink)
    )
}
