package dev.kxxcn.maru.view.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.R

class NoticeEmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        fun from(parent: ViewGroup): NoticeEmptyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.notice_empty_item, parent, false)
            return NoticeEmptyViewHolder(view)
        }
    }
}
