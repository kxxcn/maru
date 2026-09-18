package dev.kxxcn.maru.view.home

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Summary
import dev.kxxcn.maru.view.base.CapturableAdapter
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.home.holder.HomeBindingHolder

class HomeAdapter(
    @Suppress("unused") private val activity: Activity,
    private val viewModel: HomeViewModel
) : CapturableAdapter<RecyclerView.ViewHolder>() {

    var items: List<SummaryItem> = emptyList()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutRes = when (viewType) {
            TYPE_HEADER -> R.layout.home_header_item
            TYPE_BUDGET -> R.layout.home_budget_item
            TYPE_REMAIN -> R.layout.home_remain_item
            else -> R.layout.home_days_item
        }
        return HomeBindingHolder.from(parent, layoutRes)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as? HomeBindingHolder)?.bind(item.content, viewModel)
    }

    override fun getItemViewType(position: Int): Int = items[position].type

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder as? LifecycleViewHolder)?.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as? LifecycleViewHolder)?.onDetach()
        super.onViewDetachedFromWindow(holder)
    }

    override fun isSkip(viewType: Int): Boolean = false

    fun updateItems(items: List<SummaryItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_BUDGET = 1
        const val TYPE_REMAIN = 2
        const val TYPE_DAYS = 3

        fun makeItems(content: Summary): List<SummaryItem> {
            return listOf(
                SummaryItem(TYPE_HEADER, content),
                SummaryItem(TYPE_BUDGET, content),
                SummaryItem(TYPE_REMAIN, content),
                SummaryItem(TYPE_DAYS, content)
            )
        }
    }

    data class SummaryItem(val type: Int, val content: Summary)
}
