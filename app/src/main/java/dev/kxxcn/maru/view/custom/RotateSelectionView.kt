package dev.kxxcn.maru.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.AttrsUtils
import kotlinx.android.synthetic.main.rotate_selection_view.view.*
import org.jetbrains.anko.textColor

class RotateSelectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.rotate_selection_view, this)

        val ta =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.RotateSelectionView,
                defStyleAttr,
                0
            )
        rotate_selection_content.text = ta.getString(R.styleable.RotateSelectionView_text)
        ta.recycle()
    }

    fun handleFilterSelection(isSelect: Boolean) {
        if (isSelect) {
            rotate_selection_icon.visibility = View.VISIBLE
            AttrsUtils.getColor(context, R.attr.maruFontColor)
        } else {
            rotate_selection_icon.visibility = View.INVISIBLE
            AttrsUtils.getColor(context, R.attr.rotateDefaultTextColor)
        }.also {
            rotate_selection_content.textColor = it
        }
    }
}
