package dev.kxxcn.maru.view.present.category

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import dev.kxxcn.maru.GlideApp
import dev.kxxcn.maru.data.Present
import dev.kxxcn.maru.databinding.CategoryItemBinding
import dev.kxxcn.maru.view.base.LifecycleViewHolder
import dev.kxxcn.maru.view.present.PresentViewModel

class CategoryViewHolder(
    private val binding: CategoryItemBinding,
    private val size: Int
) : LifecycleViewHolder(binding) {

    fun bind(viewModel: PresentViewModel, item: Present) {
        with(binding) {
            this.lifecycleOwner = this@CategoryViewHolder
            this.viewModel = viewModel
            this.item = item
            GlideApp
                .with(itemView.context)
                .load(item.imagesRes.random())
                .transform(CenterCrop())
                .override(size)
                .into(categoryImage)
            executePendingBindings()
        }
    }

    fun clear() {
        GlideApp
            .with(itemView.context)
            .clear(binding.categoryImage)
    }

    companion object {

        fun from(parent: ViewGroup, size: Int): CategoryViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = CategoryItemBinding.inflate(inflater, parent, false)
            return CategoryViewHolder(binding, size)
        }
    }
}
