package dev.kxxcn.maru.view.custom

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.extension.px

/**
 * 가로 세그먼트 컨트롤. 라벨과 선택 개수를 보여 주고 탭으로 선택을 바꾼다.
 */
class SegmentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    data class Segment(val label: String, val count: Int? = null)

    var onSelected: ((Int) -> Unit)? = null

    var selectedIndex: Int = 0
        set(value) {
            if (field != value) {
                field = value
                render()
            }
        }

    private var segments: List<Segment> = emptyList()

    init {
        orientation = HORIZONTAL
        background = ContextCompat.getDrawable(context, R.drawable.bg_segment_track)
        val pad = 4.px
        setPadding(pad, pad, pad, pad)
    }

    fun setSegments(items: List<Segment>) {
        segments = items
        rebuild()
    }

    fun setCounts(counts: List<Int?>) {
        segments = segments.mapIndexed { index, segment -> segment.copy(count = counts.getOrNull(index)) }
        render()
    }

    private fun rebuild() {
        removeAllViews()
        segments.forEachIndexed { index, _ ->
            val view = AppCompatTextView(context).apply {
                gravity = Gravity.CENTER
                textSize = 14f
                setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                minHeight = 38.px
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    if (selectedIndex != index) {
                        selectedIndex = index
                        onSelected?.invoke(index)
                    }
                }
            }
            val params = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
            if (index > 0) params.marginStart = 2.px
            addView(view, params)
        }
        render()
    }

    private fun render() {
        segments.forEachIndexed { index, segment ->
            val view = getChildAt(index) as? AppCompatTextView ?: return@forEachIndexed
            val on = index == selectedIndex
            view.text = segment.count?.let { "${segment.label}  $it" } ?: segment.label
            view.setTextColor(
                ContextCompat.getColor(context, if (on) R.color.maru_ink else R.color.maru_muted)
            )
            view.background = if (on) {
                ContextCompat.getDrawable(context, R.drawable.bg_segment_thumb)
            } else {
                null
            }
        }
    }
}
