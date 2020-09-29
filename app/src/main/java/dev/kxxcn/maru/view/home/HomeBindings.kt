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
import dev.kxxcn.maru.view.custom.*
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

@BindingAdapter("app:primaryProgress", "app:secondaryProgress")
fun setRoundCornerProgress(
    view: RoundCornerProgressBar,
    primaryProgress: Float,
    secondaryProgress: Float
) {
    with(view) {
        this.progress = primaryProgress
        this.secondaryProgress = primaryProgress + secondaryProgress
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
        .filter { it.account != null && it.account?.remain == 0L }
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
                    setAnimation("animation_empty_2.json")
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
                val right = if (index == days.size - 1) margin else 0
                layoutParams = LinearLayout.LayoutParams(wrapContent, wrapContent).apply {
                    setMargins(margin, margin, right, margin)
                }
                bind(day)
            }
        }.forEach {
            view.addView(it)
        }
    }
}

@BindingAdapter("app:remainTransaction")
fun setRemainTransaction(view: LinearLayout, tasks: List<TaskDetail>) {
    view.removeAllViews()
    tasks
        .filter { it.account != null && it.account?.remain != 0L }
        .sortedByDescending { it.account?.date }
        .forEach {
            RemainTransactionView(view.context)
                .apply { bind(it) }
                .also { view.addView(it) }
        }
}
