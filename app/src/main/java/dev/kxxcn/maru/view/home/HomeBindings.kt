package dev.kxxcn.maru.view.home

import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import at.grabner.circleprogress.CircleProgressView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import dev.kxxcn.maru.GlideApp
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Day
import dev.kxxcn.maru.data.TaskDetail
import dev.kxxcn.maru.util.AttrsUtils
import dev.kxxcn.maru.util.ConvertUtils
import dev.kxxcn.maru.util.extension.getDisplayWidth
import dev.kxxcn.maru.util.extension.px
import dev.kxxcn.maru.view.custom.DayView
import dev.kxxcn.maru.view.custom.EmptyTransactionView
import dev.kxxcn.maru.view.custom.MaruTextView
import dev.kxxcn.maru.view.custom.TransactionView
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textColor
import org.jetbrains.anko.wrapContent
import java.text.NumberFormat
import java.util.*

@BindingAdapter("app:summaries")
fun setSummaries(view: RecyclerView, items: List<HomeAdapter.SummaryItem>?) {
    items?.let {
        (view.adapter as HomeAdapter).updateItems(items)
    }
}

@BindingAdapter("app:progress", "app:secondaryProgress")
fun setRoundProgress(view: RoundCornerProgressBar, progress: Float, secondaryProgress: Float) {
    with(view) {
        this.progress = progress
        this.secondaryProgress = progress + secondaryProgress
    }
}

@BindingAdapter("app:progressText")
fun setProgressText(view: TextView, text: String?) {
    view.text = text ?: view.context.getString(R.string.home_task_card_no_item)
}

@BindingAdapter("app:moneyText")
fun setMoneyText(view: TextView, money: Long) {
    ConvertUtils.moneyText(money).also { view.text = it }
}

@BindingAdapter("app:circleProgress")
fun setCircleProgress(view: CircleProgressView, progress: Float) {
    view.setValue(progress)
}

@BindingAdapter("app:transaction")
fun setTransaction(view: LinearLayout, tasks: List<TaskDetail>) {
    view.removeAllViews()
    val items = tasks
        .filter { it.account != null }
        .sortedByDescending { it.account?.date }
    if (items.isEmpty()) {
        view.addView(EmptyTransactionView(view.context))
    } else {
        for ((index, task) in items.withIndex()) {
            if (index > 4) return
            TransactionView(view.context)
                .apply { bind(task) }
                .also { view.addView(it) }
        }
    }
}

@BindingAdapter("app:imageResource")
fun setImageResource(view: ImageView, res: Int) {
    GlideApp.with(view)
        .load(res)
        .into(view)
}

@BindingAdapter("app:moneyFormat")
fun setMoneyFormat(view: TextView, money: Long) {
    val numberFormat = NumberFormat.getInstance(Locale.KOREA)
    view.text = numberFormat.format(money)
}

@BindingAdapter("app:daysItem")
fun setDaysItem(view: LinearLayout, days: List<Day>?) {
    view.removeAllViews()
    if (days == null || days.isEmpty()) {
        LinearLayout(view.context)
            .apply {
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent).apply {
                    width = view.context.getDisplayWidth()
                }
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
            }.also { parent ->
                parent.addView(LottieAnimationView(view.context).apply {
                    setAnimation("empty_animation_2.json")
                    repeatCount = LottieDrawable.INFINITE
                    playAnimation()
                })
                parent.addView(MaruTextView(view.context).apply {
                    text = view.context.getString(R.string.home_day_card_management_schedule)
                    textColor = AttrsUtils.getColor(view.context, R.attr.maruFontColor)
                    gravity = Gravity.CENTER
                    layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent).apply {
                        setPadding(20.px)
                    }
                })
                view.addView(parent)
            }
    } else {
        days.reversed().mapIndexed { index, day ->
            DayView(view.context).apply {
                val margin = 10.px
                val params = LinearLayout.LayoutParams(wrapContent, wrapContent)
                if (index == days.size - 1) {
                    params.setMargins(margin, margin, margin, margin)
                } else {
                    params.setMargins(margin, margin, 0, margin)
                }
                layoutParams = params
                bind(day)
            }
        }.forEach {
            view.addView(it)
        }
    }
}
