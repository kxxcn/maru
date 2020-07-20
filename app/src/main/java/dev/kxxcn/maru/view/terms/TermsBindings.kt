package dev.kxxcn.maru.view.terms

import android.widget.TextView
import androidx.databinding.BindingAdapter
import dev.kxxcn.maru.util.FileUtils

@BindingAdapter("app:rawRes")
fun setRawResource(view: TextView, res: Int) {
    view.text = FileUtils.getRawFile(view.context, res)
}
