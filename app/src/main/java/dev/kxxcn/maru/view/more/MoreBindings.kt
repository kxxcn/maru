package dev.kxxcn.maru.view.more

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.util.CONTENTS_ITEM_SPAN_COUNT
import dev.kxxcn.maru.view.custom.DrawableTextView
import dev.kxxcn.maru.view.more.contents.ContentsItem
import dev.kxxcn.maru.view.more.contents.MoreContentsAdapter

@BindingAdapter("app:drawableIcon", "app:drawableText")
fun setDrawableArguments(view: DrawableTextView, iconRes: Int, textRes: Int) {
    view.setDrawableArguments(iconRes, textRes)
}

@BindingAdapter("app:moreItems")
fun setMoreItems(view: RecyclerView, items: List<MoreAdapter.MoreItem>?) {
    items?.let {
        with(view) {
            (adapter as? MoreAdapter)?.updateItems(it)
        }
    }
}

@BindingAdapter("app:contentsItems")
fun setContentsItems(view: RecyclerView, items: List<ContentsItem>?) {
    items?.let {
        with(view) {
            layoutManager = GridLayoutManager(context, CONTENTS_ITEM_SPAN_COUNT)
            (adapter as? MoreContentsAdapter)?.updateItems(it)
        }
    }
}
