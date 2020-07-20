package dev.kxxcn.maru.view.present.description

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import dev.kxxcn.maru.data.Present
import dev.kxxcn.maru.util.KEY_PRESENT_IMAGE_RES

class DescriptionPagerAdapter(
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var present: Present? = null

    override fun getItem(position: Int): Fragment {
        return DescriptionPagerFragment().apply {
            arguments = bundleOf(
                KEY_PRESENT_IMAGE_RES to present?.imagesRes?.get(position)
            )
        }
    }

    override fun getCount() = present?.imagesRes?.size ?: 0

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun notify(present: Present?) {
        this.present = present
        notifyDataSetChanged()
    }
}
