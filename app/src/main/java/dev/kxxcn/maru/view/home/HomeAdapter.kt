package dev.kxxcn.maru.view.home

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.data.Summary
import dev.kxxcn.maru.view.base.CapturableAdapter
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.home.holder.*

class HomeAdapter(
    private val activity: Activity,
    private val viewModel: HomeViewModel
) : CapturableAdapter<RecyclerView.ViewHolder>() {

    private val releasable = mutableListOf<() -> Unit>()

    var items: List<SummaryItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_WELCOME -> WelcomeHolder.from(parent)
            TYPE_ACCOUNT -> AccountHolder.from(parent)
            TYPE_WOOZOORA -> WoozooraHolder.from(parent)
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
                h.bind(item, viewModel)
            }
            TYPE_ACCOUNT -> {
                val h = holder as? AccountHolder ?: return
                h.bind(item)
            }
            TYPE_WOOZOORA -> {
                val h = holder as? WoozooraHolder ?: return
                h.bind(viewModel)
            }
            TYPE_TASK -> {
                val h = holder as? TaskHolder ?: return
                h.bind(item)
            }
            TYPE_BANNER_AD -> {
                val h = holder as? BannerAdHolder ?: return
                releasable.add(h.loadAd())
            }
            TYPE_DAYS -> {
                val h = holder as? DaysHolder ?: return
                releasable.add(h.bind(item, viewModel))
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

    override fun isSkip(viewType: Int): Boolean {
        return when (viewType) {
            TYPE_WELCOME,
            TYPE_BANNER_AD -> true
            TYPE_DAYS -> items
                ?.firstOrNull()
                ?.content
                ?.hasDays == false
            else -> false
        }
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

        const val TYPE_WELCOME = 0
        const val TYPE_ACCOUNT = 1
        const val TYPE_WOOZOORA = 2
        const val TYPE_TASK = 3
        const val TYPE_BANNER_AD = 4
        const val TYPE_DAYS = 5

        fun makeItems(content: Summary): List<SummaryItem> {
            return mutableListOf<SummaryItem>().apply {
                add(SummaryItem(TYPE_WELCOME, content))
                add(SummaryItem(TYPE_ACCOUNT, content))
                add(SummaryItem(TYPE_WOOZOORA, content))
                add(SummaryItem(TYPE_TASK, content))
                if (content.user?.premium == false) add(SummaryItem(TYPE_BANNER_AD, content))
                add(SummaryItem(TYPE_DAYS, content))
            }
        }
    }

    data class SummaryItem(val type: Int, val content: Summary)
}
