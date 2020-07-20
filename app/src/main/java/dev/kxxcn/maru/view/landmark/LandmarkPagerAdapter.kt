package dev.kxxcn.maru.view.landmark

import android.location.Location
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import dev.kxxcn.maru.util.KEY_LANDMARK_FILTER_TYPE
import dev.kxxcn.maru.util.KEY_LANDMARK_LATITUDE
import dev.kxxcn.maru.util.KEY_LANDMARK_LONGITUDE

class LandmarkPagerAdapter(
    fm: FragmentManager,
    private val location: Location?
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val item = LandmarkFilterType.values()[position]
        return LandmarkPagerFragment().apply {
            arguments = bundleOf(
                KEY_LANDMARK_FILTER_TYPE to item,
                KEY_LANDMARK_LONGITUDE to location?.longitude,
                KEY_LANDMARK_LATITUDE to location?.latitude
            )
        }
    }

    override fun getCount(): Int {
        return LandmarkFilterType.values().size
    }
}
