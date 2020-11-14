package dev.kxxcn.maru.view.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.support.DaggerFragment
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.util.AnalyticsUtils
import dev.kxxcn.maru.util.extension.setupSnackbar
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    abstract val clazz: Class<*>

    abstract val viewModel: ViewModel

    protected var alertDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupSnackbar()
        setupAnalytics()
    }

    override fun onDestroyView() {
        alertDialog?.dismiss()
        alertDialog = null
        super.onDestroyView()
    }

    private fun setupListener() {
        (viewModel as? BaseViewModel)?.let {
            it.closeEvent.observe(viewLifecycleOwner, EventObserver {
                findNavController().popBackStack()
            })
        }
    }

    private fun setupSnackbar() {
        (viewModel as? BaseViewModel)?.let {
            view?.setupSnackbar(
                viewLifecycleOwner,
                it.snackbarRes
            )
            view?.setupSnackbar(
                viewLifecycleOwner,
                it.snackbarText
            )
        }
    }

    private fun setupAnalytics() {
        activity?.let {
            firebaseAnalytics.setCurrentScreen(
                it,
                AnalyticsUtils.screenName(clazz.name),
                null
            )
        }
    }
}
