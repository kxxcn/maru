package dev.kxxcn.maru.view.notice

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Notice
import dev.kxxcn.maru.databinding.NoticeItemBinding
import dev.kxxcn.maru.util.EXPAND_ANIMATE_DURATION
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import org.jetbrains.anko.sdk27.coroutines.onClick

class NoticeViewHolder(
    private val binding: NoticeItemBinding
) : LifecycleViewHolder(binding) {

    private var heightAnimator: ValueAnimator? = null
    private var rotateAnimator: ValueAnimator? = null

    init {
        itemView.onClick {
            val contentView = binding.noticeContentParent
            val expandIcon = binding.noticeExpand

            val isExpand = contentView
                .getTag(R.id.tag_notice_item_content_expand) as? Boolean
                ?: false

            (contentView.getTag(R.id.tag_notice_item_content_animator) as? ValueAnimator)
                ?.takeIf { it.isRunning }
                ?.cancel()

            (expandIcon.getTag(R.id.tag_notice_item_rotate_animator) as? ValueAnimator)
                ?.takeIf { it.isRunning }
                ?.cancel()

            if (isExpand) {
                ValueAnimator.ofInt(
                    contentView.height,
                    0
                ) to ValueAnimator.ofFloat(expandIcon.rotation, 0f)
            } else {
                contentView.measure(0, 0)
                ValueAnimator.ofInt(
                    contentView.height,
                    contentView.measuredHeight
                ) to ValueAnimator.ofFloat(expandIcon.rotation, 180f)
            }.also {
                heightAnimator = it.first
                rotateAnimator = it.second
            }

            heightAnimator?.let {
                it.duration = EXPAND_ANIMATE_DURATION
                it.addUpdateListener { anim -> setContentViewHeight(anim.animatedValue as Int) }
                it.start()
                contentView.setTag(R.id.tag_notice_item_content_animator, it)
            }
            rotateAnimator?.let {
                it.duration = EXPAND_ANIMATE_DURATION
                it.addUpdateListener { anim -> setExpandIconRotation(anim.animatedValue as Float) }
                it.start()
                expandIcon.setTag(R.id.tag_notice_item_rotate_animator, it)
            }

            contentView.setTag(R.id.tag_notice_item_content_expand, !isExpand)

            val parent = itemView.parent as? RecyclerView ?: return@onClick
            val adapter = parent.adapter as? NoticeAdapter ?: return@onClick
            adapter.put(adapterPosition, !isExpand)
        }
    }

    private fun setContentViewHeight(height: Int) {
        with(binding.noticeContentParent) {
            layoutParams = layoutParams.apply {
                this.height = height
            }
        }
    }

    private fun setExpandIconRotation(rotation: Float) {
        binding.noticeExpand.rotation = rotation
    }

    private fun release() {
        heightAnimator?.removeAllUpdateListeners()
        rotateAnimator?.removeAllUpdateListeners()
        heightAnimator = null
        rotateAnimator = null
    }

    fun bind(notice: Notice, isExpand: Boolean): () -> Unit {
        with(binding) {
            with(noticeContentParent) {
                if (isExpand) {
                    measure(0, 0)
                    layoutParams.height = measuredHeight
                    noticeExpand.rotation = 180f
                } else {
                    layoutParams.height = 0
                    noticeExpand.rotation = 0f
                }
            }
            this.lifecycleOwner = this@NoticeViewHolder
            this.notice = notice
            this.executePendingBindings()
        }
        return { release() }
    }

    companion object {

        fun from(parent: ViewGroup): NoticeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = NoticeItemBinding.inflate(inflater, parent, false)
            return NoticeViewHolder(binding)
        }
    }
}
