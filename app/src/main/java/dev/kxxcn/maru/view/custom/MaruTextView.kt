package dev.kxxcn.maru.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import dev.kxxcn.maru.R

class MaruTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.colorPrimary)
        alpha = 80
    }

    var highlight = false
        private set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private var rect: Rect? = null

    init {
        val normal = MaruTextViewType.NORMAL.ordinal

        val ta = context.obtainStyledAttributes(
            attrs,
            R.styleable.MaruTextView,
            defStyleAttr,
            0
        )
        val value = ta.getInt(R.styleable.MaruTextView_textStyle, normal)

        highlight = ta.getBoolean(R.styleable.MaruTextView_highlight, false)

        val tf = Typeface.createFromAsset(context.assets, "nixgon.ttf")
        val style = if (value == normal) Typeface.NORMAL else Typeface.BOLD
        setTypeface(tf, style)

        ta.recycle()

        if (highlight) {
            viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
                        rect = Rect(
                            measuredWidth / 8,
                            measuredHeight / 3,
                            measuredWidth,
                            measuredHeight
                        )
                    }
                }
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (highlight) {
            setMeasuredDimension(measuredWidth + 30, measuredHeight)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val r = rect
        if (highlight && r != null) {
            canvas?.drawRect(r, paint)
        }
    }
}

enum class MaruTextViewType { NORMAL, BOLD }
