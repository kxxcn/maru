package dev.kxxcn.maru.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.DrawableTextViewBinding
import dev.kxxcn.maru.util.AttrsUtils
import dev.kxxcn.maru.util.extension.setTint

class DrawableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: DrawableTextViewBinding?

    init {
        val ta = context.obtainStyledAttributes(
            attrs,
            R.styleable.DrawableTextView,
            defStyleAttr,
            0
        )

        val inflater = LayoutInflater.from(context)
        binding = DrawableTextViewBinding.inflate(
            inflater,
            this,
            true
        ).apply {
            this.icon =
                ta.getDrawable(R.styleable.DrawableTextView_drawable_src) ?: return@apply
            this.text = ta.getString(R.styleable.DrawableTextView_drawable_text) ?: return@apply
        }

        ta.getBoolean(R.styleable.DrawableTextView_drawable_theme, false)
            .takeIf { it }
            ?.let {
                binding?.drawableIcon?.setTint(
                    AttrsUtils.getColor(
                        context,
                        R.attr.maruFontColor
                    )
                )
            }

        ta.recycle()
    }

    fun setDrawableArguments(iconRes: Int, textRes: Int) {
        binding?.icon = ContextCompat.getDrawable(context, iconRes)
        binding?.text = context.getString(textRes)
    }

    fun release() {
        binding = null
    }
}
