package dev.kxxcn.maru.view.intro

import android.widget.TextView
import androidx.core.text.underline
import androidx.databinding.BindingAdapter
import dev.kxxcn.maru.util.extension.bold
import org.jetbrains.anko.buildSpanned

@BindingAdapter("app:bold")
fun setBoldText(view: TextView, text: String) {
    view.text = buildSpanned {
        bold(0, 2) {
            append(text)
        }
    }
}

@BindingAdapter("app:underline")
fun setUnderlineText(view: TextView, text: String) {
    view.text = buildSpanned {
        underline {
            append(text)
        }
    }
}
