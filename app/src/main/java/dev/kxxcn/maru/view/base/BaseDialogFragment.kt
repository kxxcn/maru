package dev.kxxcn.maru.view.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.support.DaggerDialogFragment
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.util.AnalyticsUtils
import dev.kxxcn.maru.util.extension.setupSnackbar
import javax.inject.Inject

abstract class BaseDialogFragment : DaggerDialogFragment() {

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    abstract val clazz: Class<*>

    abstract val viewModel: ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupSnackbar()
        setupAnalytics()
    }

    private fun setupListener() {
        (viewModel as? BaseViewModel)?.let {
            it.closeEvent.observe(viewLifecycleOwner, EventObserver {
                findNavController().popBackStack()
            })
            it.toastText.observe(viewLifecycleOwner, EventObserver { message ->
                toast(message)
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

    private fun toast(any: Any) {
        if (any is Int) {
            Toast.makeText(context, any, Toast.LENGTH_SHORT).show()
        } else if (any is String) {
            Toast.makeText(context, any, Toast.LENGTH_SHORT).show()
        }
    }
}
