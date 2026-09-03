package dev.kxxcn.maru.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.RotateSelectionViewBinding
import dev.kxxcn.maru.util.AttrsUtils
import org.jetbrains.anko.textColor

class RotateSelectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = RotateSelectionViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val ta =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.RotateSelectionView,
                defStyleAttr,
                0
            )
        binding.rotateSelectionContent.text = ta.getString(R.styleable.RotateSelectionView_text)
        ta.recycle()
    }

    fun handleFilterSelection(isSelect: Boolean) {
        if (isSelect) {
            binding.rotateSelectionIcon.visibility = View.VISIBLE
            AttrsUtils.getColor(context, R.attr.maruFontColor)
        } else {
            binding.rotateSelectionIcon.visibility = View.INVISIBLE
            AttrsUtils.getColor(context, R.attr.rotateDefaultTextColor)
        }.also {
            binding.rotateSelectionContent.textColor = it
        }
    }

    fun getSelectionContent(): String {
        return binding.rotateSelectionContent.text.toString()
    }
}
