package dev.kxxcn.maru.view.custom

import androidx.core.content.ContextCompat
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.UnderlineViewBinding
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
            ContextCompat.getColor(context, R.color.maru_ink)
        } else {
            binding.underline.visibility = View.INVISIBLE
            ContextCompat.getColor(context, R.color.maru_muted)
        }.also {
            binding.underlineText.textColor = it
        }
    }
}
