package dev.kxxcn.maru.view.base

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.support.DaggerFragment
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.AnalyticsUtils
import dev.kxxcn.maru.util.extension.applySystemBarsPadding
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
        view.applySystemBarsPadding()
        setupListener()
        setupSnackbar()
        setupAnalytics()
    }

    override fun onDestroyView() {
        alertDialog?.dismiss()
        alertDialog = null
        super.onDestroyView()
    }

    fun openStore(packageName: String?) {
        try {
            val uri = getString(R.string.play_store_app, packageName)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        } catch (e: ActivityNotFoundException) {
            val uri = getString(R.string.play_store_web, packageName)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        }
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
