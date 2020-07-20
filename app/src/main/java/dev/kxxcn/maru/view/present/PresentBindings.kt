package dev.kxxcn.maru.view.present

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dev.kxxcn.maru.GlideApp
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Present
import dev.kxxcn.maru.util.AttrsUtils
import dev.kxxcn.maru.util.extension.px
import dev.kxxcn.maru.view.present.category.PresentAdapter
import org.jetbrains.anko.textColor

@BindingAdapter("app:presentItems")
fun setPresentItems(view: RecyclerView, items: List<Present>?) {
    items?.let {
        with(view) {
            (adapter as? PresentAdapter)?.submitList(it)
        }
    }
}

@ExperimentalStdlibApi
@BindingAdapter("app:presentImage")
fun setPresentImage(view: ImageView, item: Present) {
    item.imagesRes.randomOrNull()?.let {
        GlideApp.with(view)
            .load(it)
            .transform(CenterCrop(), RoundedCorners(15.px))
            .into(view)
    }
}

@BindingAdapter("app:descText")
fun setDescText(view: TextView, textRes: Int?) {
    textRes?.let {
        val context = view.context
        view.text = context?.getString(it)
    }
}
