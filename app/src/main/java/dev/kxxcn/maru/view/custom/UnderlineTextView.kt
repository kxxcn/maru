package dev.kxxcn.maru.view.custom

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.AttrsUtils
import kotlinx.android.synthetic.main.underline_view.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor

class UnderlineTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.underline_view, this)

        val ta = context.obtainStyledAttributes(
            attrs,
            R.styleable.UnderlineTextView,
            defStyleAttr,
            0
        )
        underline_text.text = ta.getString(R.styleable.UnderlineTextView_underline_text)
        ta.recycle()
    }

    fun handleFilterSelection(isSelect: Boolean) {
        if (isSelect) {
            underline.visibility = View.VISIBLE
            AttrsUtils.getColor(context, R.attr.rotateSelectionSelectText)
        } else {
            underline.visibility = View.INVISIBLE
            AttrsUtils.getColor(context, R.attr.rotateSelectionDefaultText)
        }.also {
            underline_text.textColor = it
        }
    }
}
