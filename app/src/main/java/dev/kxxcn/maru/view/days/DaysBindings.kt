package dev.kxxcn.maru.view.days

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.data.Day
import dev.kxxcn.maru.util.ConvertUtils
import dev.kxxcn.maru.view.days.DaysFilterType.COUNT

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

@BindingAdapter("app:days")
fun setDays(view: TextView, day: Day) {
    if (day.type == COUNT) {
        ConvertUtils.getCount(day.date)
    } else {
        ConvertUtils.getRemain(day.date)
    }.also { (res, count) ->
        view.text = view.context.getString(res, count)
    }
}
