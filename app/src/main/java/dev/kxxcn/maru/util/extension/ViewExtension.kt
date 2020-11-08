package dev.kxxcn.maru.util.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.collection.LruCache
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.kxxcn.maru.Event
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.Capturable
import dev.kxxcn.maru.view.home.HomeAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.wrapContent

fun Fragment.displayWidth() = activity
    ?.getDisplaySize()
    ?.widthPixels
    ?: throw RuntimeException("Invalid Activity.")

fun Fragment.displayHeight() = activity
    ?.getDisplaySize()
    ?.heightPixels
    ?: throw RuntimeException("Invalid Activity.")

fun Fragment.openDialog(
    iconRes: Int,
    text: String,
    negative: (() -> Unit)? = null,
    positive: (() -> Unit)? = null
): AlertDialog? {
    val context = context ?: return null
    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.decision_selection_view, null).also {
        it.findViewById<ImageView>(R.id.decision_icon).imageResource = iconRes
        it.findViewById<TextView>(R.id.decision_text).text = text
        it.findViewById<TextView>(R.id.decision_no).onClick { negative?.invoke() }
        it.findViewById<TextView>(R.id.decision_yes).onClick { positive?.invoke() }
    }
    return AlertDialog.Builder(context)
        .setView(view)
        .create()
        .apply {
            show()
            window?.let {
                it.setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        context,
                        android.R.color.transparent
                    )
                )
                it.setLayout((displayWidth() * 0.6).toInt(), wrapContent)
            }
        }
}

inline fun MotionLayout.setTransitionCompleteListener(
    crossinline onTransitionTrigger: (
        p0: MotionLayout?,
        p1: Int,
        p2: Boolean,
        p3: Float
    ) -> Unit = { _, _, _, _ -> },
    crossinline onTransitionStarted: (
        p0: MotionLayout?,
        p1: Int,
        p2: Int
    ) -> Unit = { _, _, _ -> },
    crossinline onTransitionChange: (
        p0: MotionLayout?,
        p1: Int,
        p2: Int,
        p3: Float
    ) -> Unit = { _, _, _, _ -> },
    crossinline onTransitionCompleted: (
        p0: MotionLayout?,
        p1: Int
    ) -> Unit = { _, _ -> }
): MotionLayout.TransitionListener {
    val listener = object : MotionLayout.TransitionListener {
        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            onTransitionTrigger.invoke(p0, p1, p2, p3)
        }

        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            onTransitionStarted.invoke(p0, p1, p2)
        }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            onTransitionChange.invoke(p0, p1, p2, p3)
        }

        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            onTransitionCompleted.invoke(p0, p1)
        }
    }
    setTransitionListener(listener)
    return listener
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.showKeyboard() {
    view?.let { activity?.showKeyboard(it) }
}

fun Activity.showKeyboard() {
    showKeyboard(currentFocus ?: View(this))
}

fun Context.showKeyboard(view: View) {
    try {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager? ?: return
        view.requestFocus()
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    } catch (ignore: Exception) {
    }
}

fun View.showSnackbar(text: String, timeLength: Int) {
    Snackbar.make(this, text, timeLength).show()
}

fun <T> View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<T>>,
    timeLength: Int = Snackbar.LENGTH_SHORT
) {
    snackbarEvent.observe(lifecycleOwner, { event ->
        event.getContentIfNotHandled()?.let {
            if (it is Int) {
                showSnackbar(context.getString(it), timeLength)
            } else if (it is String) {
                showSnackbar(it, timeLength)
            }
        }
    })
}

fun View.requestNewSize(width: Int? = null, height: Int? = null) {
    width?.let { layoutParams.width = it }
    height?.let { layoutParams.height = it }
    layoutParams = layoutParams
}

fun View.asTextView() = this as TextView

fun View.asImageView() = this as ImageView

suspend fun RecyclerView.capture(): Bitmap? {
    var bitmap: Bitmap? = null
    (adapter as? HomeAdapter)?.let { adapter ->
        suppressLayout(true)

        var height = 0
        // 홀더 간 간격
        val spacingBetweenHolder = 30.px
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        val cache = LruCache<String, Bitmap>(cacheSize)

        fun isPassedHolder(viewType: Int): Boolean {
            return when (viewType) {
                HomeAdapter.TYPE_WELCOME,
                HomeAdapter.TYPE_BANNER_AD -> true
                HomeAdapter.TYPE_DAYS -> adapter.items
                    ?.firstOrNull()
                    ?.content
                    ?.hasDays == false
                else -> false
            }
        }

        val widthMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        val size = adapter.itemCount
        for (position in 0 until size) {
            val viewType = adapter.getItemViewType(position)
            if (isPassedHolder(viewType)) continue
            val holder = withContext(Dispatchers.Main) {
                with(adapter) {
                    createViewHolder(
                        this@capture,
                        viewType
                    ).also { holder ->
                        onBindViewHolder(holder, position)
                        (holder as? Capturable)?.capture()

                        holder.itemView.measure(widthMeasureSpec, heightMeasureSpec)
                        holder.itemView.layout(
                            0,
                            0,
                            holder.itemView.measuredWidth,
                            holder.itemView.measuredHeight
                        )
                    }
                }
            }

            cache.put(position.toString(), holder.itemView.drawToBitmap())
            height += holder.itemView.measuredHeight + spacingBetweenHolder
        }

        suppressLayout(false)

        createBitmap(measuredWidth, height, Bitmap.Config.ARGB_8888)
            .also { bitmap = it }
            .let {
                val canvas = Canvas(it)
                val color = if (PreferenceUtils.useDarkMode) {
                    ContextCompat.getColor(context, R.color.colorPrimaryDarkNight)
                } else {
                    Color.WHITE
                }
                canvas.drawColor(color)

                // 캔버스 상 다음 홀더가 그려질 높이(y 좌표)
                var offset = spacingBetweenHolder
                val paint = Paint()

                for (position in 0 until size) {
                    cache[position.toString()]?.let { itemView ->
                        canvas.drawBitmap(itemView, 0f, offset.toFloat(), paint)
                        offset += itemView.height + spacingBetweenHolder
                        itemView.recycle()
                    }
                }
            }
    }

    return bitmap
}
