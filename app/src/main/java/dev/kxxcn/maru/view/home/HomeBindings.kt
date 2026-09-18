package dev.kxxcn.maru.view.home

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.GlideApp
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Day
import dev.kxxcn.maru.util.ConvertUtils
import dev.kxxcn.maru.util.DateUtils
import dev.kxxcn.maru.util.extension.px
import dev.kxxcn.maru.view.days.dayNumberText
import dev.kxxcn.maru.view.days.isPastDay
import java.text.NumberFormat
import java.util.Locale

@BindingAdapter("app:homeItems")
fun setHomeItems(view: RecyclerView, items: List<HomeAdapter.SummaryItem>?) {
    items?.let { (view.adapter as? HomeAdapter)?.updateItems(it) }
}

@BindingAdapter("app:moneyText")
fun setMoneyText(view: TextView, money: Long) {
    view.text = ConvertUtils.moneyText(money)
}

@BindingAdapter("app:moneyFormat")
fun setMoneyFormat(view: TextView, money: Long) {
    view.text = NumberFormat.getInstance(Locale.KOREA).format(money)
}

@BindingAdapter("app:imageResource")
fun setImageResource(view: ImageView, res: Int) {
    GlideApp.with(view)
        .load(res)
        .into(view)
}

/**
 * 홈의 디데이 칩. 날짜순으로 나열하고, 없으면 등록 안내 칩 하나를 둔다.
 */
@BindingAdapter(value = ["app:dayChips", "app:dayChipsClick"], requireAll = false)
fun setDayChips(view: LinearLayout, days: List<Day>?, listener: View.OnClickListener?) {
    view.removeAllViews()
    val inflater = LayoutInflater.from(view.context)
    val sorted = days.orEmpty().sortedBy { it.date }
    if (sorted.isEmpty()) {
        val chip = inflater.inflate(R.layout.home_day_chip, view, false)
        chip.findViewById<TextView>(R.id.chip_number).text = "+"
        chip.findViewById<TextView>(R.id.chip_title).text = view.context.getString(R.string.home_days_add)
        chip.findViewById<TextView>(R.id.chip_date).text = view.context.getString(R.string.home_day_card_title)
        chip.setOnClickListener(listener)
        view.addView(chip)
        return
    }
    sorted.forEachIndexed { index, day ->
        val chip = inflater.inflate(R.layout.home_day_chip, view, false)
        val number = chip.findViewById<TextView>(R.id.chip_number)
        number.text = dayNumberText(view.context, day)
        number.setTextColor(ContextCompat.getColor(view.context, if (isPastDay(day)) R.color.maru_muted else R.color.maru_ink))
        chip.findViewById<TextView>(R.id.chip_title).text = day.content
        chip.findViewById<TextView>(R.id.chip_date).text = DateUtils.DATE_FORMAT_8.format(day.date)
        chip.setOnClickListener(listener)
        (chip.layoutParams as? LinearLayout.LayoutParams)?.marginStart = if (index == 0) 0 else 10.px
        view.addView(chip)
    }
}
