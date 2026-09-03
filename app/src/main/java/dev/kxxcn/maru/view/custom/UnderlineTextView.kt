package dev.kxxcn.maru.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.UnderlineViewBinding
import dev.kxxcn.maru.util.AttrsUtils
import org.jetbrains.anko.textColor

class UnderlineTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = UnderlineViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val ta = context.obtainStyledAttributes(
            attrs,
            R.styleable.UnderlineTextView,
            defStyleAttr,
            0
        )
        binding.underlineText.text = ta.getString(R.styleable.UnderlineTextView_underline_text)
        ta.recycle()
    }

    fun handleFilterSelection(isSelect: Boolean) {
        if (isSelect) {
            binding.underline.visibility = View.VISIBLE
            AttrsUtils.getColor(context, R.attr.rotateSelectionSelectText)
        } else {
            binding.underline.visibility = View.INVISIBLE
            AttrsUtils.getColor(context, R.attr.rotateSelectionDefaultText)
        }.also {
            binding.underlineText.textColor = it
        }
    }
}
