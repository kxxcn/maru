package dev.kxxcn.maru.view.onboard.page

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import dev.kxxcn.maru.util.KEY_ONBOARD_TYPE

class OnboardPagerAdapter(
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = 3

    override fun getItem(position: Int): Fragment {
        return OnboardPagerFragment().apply {
            arguments = bundleOf(KEY_ONBOARD_TYPE to position)
        }
    }
}
