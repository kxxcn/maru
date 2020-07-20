package dev.kxxcn.maru.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import dev.kxxcn.maru.util.extension.px
import dev.kxxcn.maru.view.days.DaysViewHolder

class LinearSpacingDecoration(
    private val size: Int = 30.px
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)
        with(outRect) {
            top = size
            if (position == state.itemCount - 1) {
                bottom = size
            }
        }
    }
}

class GridSpacingDecoration(
    private val size: Int = 25.px,
    private val spanCount: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)
        with(outRect) {
            if (position < spanCount) return@with
            top = size
        }
    }
}

class DayGridSpacingDecoration(
    private val size: Int = 10.px
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        with(outRect) {
            left = size
            right = size
            bottom = size
        }
    }
}

class ItemTouchHelperCallback(
    private val adapter: ItemTouchHelperAdapter
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder?.itemView?.alpha = 0.5f
            adapter.onItemSelected(viewHolder)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        viewHolder.itemView.alpha = 1f
        adapter.onItemClear(viewHolder)
        super.clearView(recyclerView, viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    interface ItemTouchHelperAdapter {

        fun onItemMove(fromPosition: Int, toPosition: Int)

        fun onItemSelected(viewHolder: RecyclerView.ViewHolder?)

        fun onItemClear(viewHolder: RecyclerView.ViewHolder?)
    }
}

interface OnStartDragListener {

    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}
