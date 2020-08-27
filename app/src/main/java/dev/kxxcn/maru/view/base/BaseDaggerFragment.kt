package dev.kxxcn.maru.view.base

import android.os.Bundle
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.support.DaggerFragment
import dev.kxxcn.maru.util.AnalyticsUtils
import javax.inject.Inject

abstract class BaseDaggerFragment : DaggerFragment() {

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    abstract val clazz: Class<*>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            firebaseAnalytics.setCurrentScreen(
                it,
                AnalyticsUtils.screenName(clazz.name),
                null
            )
        }
    }
}
