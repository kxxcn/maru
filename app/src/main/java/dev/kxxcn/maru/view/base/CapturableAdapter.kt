package dev.kxxcn.maru.view.base

import androidx.recyclerview.widget.RecyclerView

abstract class CapturableAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    abstract fun isSkip(viewType: Int):Boolean
}
