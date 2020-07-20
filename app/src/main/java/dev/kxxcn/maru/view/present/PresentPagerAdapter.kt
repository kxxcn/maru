package dev.kxxcn.maru.view.present

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import dev.kxxcn.maru.view.present.category.CategoryFragment
import dev.kxxcn.maru.view.present.description.DescriptionFragment

class PresentPagerAdapter(
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> CategoryFragment()
            else -> DescriptionFragment()
        }
    }

    override fun getCount() = 2
}
