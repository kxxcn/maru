package dev.kxxcn.maru.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import dev.kxxcn.maru.R

/**
 * 신랑(청)과 신부(홍) 분담 비율을 한 줄로 보여 주는 바.
 */
class SplitBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.maru_surface_2)
    }
    private val groomPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.maru_groom)
    }
    private val bridePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.maru_bride)
    }
    private val rect = RectF()
    private val path = Path()
    private var groom = 0f
    private var bride = 0f

    fun setProgress(groomRatio: Float, brideRatio: Float) {
        groom = groomRatio.coerceIn(0f, 1f)
        bride = brideRatio.coerceIn(0f, 1f - groom)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()
        if (w <= 0f || h <= 0f) return
        val radius = h / 2f
        rect.set(0f, 0f, w, h)
        path.reset()
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        canvas.drawPath(path, trackPaint)
        canvas.save()
        canvas.clipPath(path)
        val groomEnd = w * groom
        canvas.drawRect(0f, 0f, groomEnd, h, groomPaint)
        canvas.drawRect(groomEnd, 0f, groomEnd + w * bride, h, bridePaint)
        canvas.restore()
    }
}
