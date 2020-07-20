package dev.kxxcn.maru.view.more

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.home.holder.BannerAdHolder
import dev.kxxcn.maru.view.more.contents.MoreContentsHolder
import dev.kxxcn.maru.view.more.native.MoreNativeHolder
import dev.kxxcn.maru.view.more.setting.MoreSettingHolder

class MoreAdapter(
    private val viewModel: MoreViewModel,
    private val activity: Activity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<MoreItem>? = null

    private val releasable = mutableListOf<() -> Unit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SETTINGS -> MoreSettingHolder.from(parent)
            TYPE_BANNER_AD -> BannerAdHolder.from(parent, activity)
            TYPE_CONTENTS -> MoreContentsHolder.from(parent)
            TYPE_NATIVE_AD -> MoreNativeHolder.from(parent)
            else -> MoreSettingHolder.from(parent)
        }
    }

    override fun getItemCount() = items?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return items?.get(position)?.viewType ?: TYPE_SETTINGS
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items?.get(position) ?: return
        when (item.viewType) {
            TYPE_SETTINGS -> {
                val h = holder as? MoreSettingHolder
                    ?: return
                releasable.add(h.bind(viewModel))
            }
            TYPE_CONTENTS -> {
                val h = holder as? MoreContentsHolder
                    ?: return
                releasable.add(h.bind(viewModel))
            }
            TYPE_NATIVE_AD -> {
                val h = holder as? MoreNativeHolder ?: return
                releasable.add(h.loadAd())
            }
            TYPE_BANNER_AD -> {
                val h = holder as? BannerAdHolder ?: return
                releasable.add(h.loadAd())
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder as? LifecycleViewHolder)?.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as? LifecycleViewHolder)?.onDetach()
        super.onViewDetachedFromWindow(holder)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        release()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    private fun release() {
        releasable.forEach { release -> release() }
        releasable.clear()
    }

    fun updateItems(items: List<MoreItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    companion object {

        private const val TYPE_SETTINGS = 0
        private const val TYPE_BANNER_AD = 1
        private const val TYPE_CONTENTS = 2
        private const val TYPE_NATIVE_AD = 3

        fun makeItems(): List<MoreItem> {
            return listOf(
                MoreItem(TYPE_SETTINGS),
                MoreItem(TYPE_BANNER_AD),
                MoreItem(TYPE_CONTENTS),
                MoreItem(TYPE_NATIVE_AD)
            )
        }
    }

    data class MoreItem(val viewType: Int)
}
