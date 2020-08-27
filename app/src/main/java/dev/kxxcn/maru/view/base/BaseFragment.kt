package dev.kxxcn.maru.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import dev.kxxcn.maru.util.AnalyticsUtils

abstract class BaseFragment : Fragment() {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    abstract val clazz: Class<*>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { firebaseAnalytics = FirebaseAnalytics.getInstance(it) }
        activity?.let {
            firebaseAnalytics?.setCurrentScreen(
                it,
                AnalyticsUtils.screenName(clazz.name),
                null
            )
        }
    }
}
