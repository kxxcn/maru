package dev.kxxcn.maru.view.home

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.data.Summary
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.home.holder.*

class HomeAdapter(
    private val activity: Activity,
    private val viewModel: HomeViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<SummaryItem>? = null

    private val releasable = mutableListOf<() -> Unit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_WELCOME -> WelcomeHolder.from(parent)
            TYPE_ACCOUNT -> AccountHolder.from(parent)
            TYPE_TASK -> TaskHolder.from(parent)
            TYPE_BANNER_AD -> BannerAdHolder.from(parent, activity)
            else -> DaysHolder.from(parent)
        }
    }

    override fun getItemCount() = items?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items?.get(position) ?: return
        when (item.type) {
            TYPE_WELCOME -> {
                val h = holder as? WelcomeHolder ?: return
                releasable.add(h.bind(item, viewModel))
            }
            TYPE_ACCOUNT -> {
                val h = holder as? AccountHolder ?: return
                h.bind(item)
            }
            TYPE_TASK -> {
                val h = holder as? TaskHolder ?: return
                releasable.add(h.bind(item))
            }
            TYPE_DAYS -> {
                val h = holder as? DaysHolder ?: return
                releasable.add(h.bind(item))
            }
            TYPE_BANNER_AD -> {
                val h = holder as? BannerAdHolder ?: return
                releasable.add(h.loadAd())
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items?.get(position)?.type ?: TYPE_WELCOME
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

    fun updateItems(items: List<SummaryItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    private fun release() {
        releasable.forEach { release -> release() }
        releasable.clear()
    }

    companion object {

        private const val TYPE_WELCOME = 0
        private const val TYPE_ACCOUNT = 1
        private const val TYPE_TASK = 2
        private const val TYPE_DAYS = 3
        private const val TYPE_BANNER_AD = 4

        fun makeItems(content: Summary): List<SummaryItem> {
            return mutableListOf<SummaryItem>().apply {
                add(SummaryItem(TYPE_WELCOME, content))
                add(SummaryItem(TYPE_ACCOUNT, content))
                add(SummaryItem(TYPE_TASK, content))
                if (content.user?.premium == false) add(SummaryItem(TYPE_BANNER_AD, content))
                add(SummaryItem(TYPE_DAYS, content))
            }
        }
    }

    data class SummaryItem(val type: Int, val content: Summary)
}
