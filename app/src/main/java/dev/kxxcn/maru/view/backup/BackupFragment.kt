package dev.kxxcn.maru.view.backup

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.android.support.DaggerFragment
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.BackupFragmentBinding
import dev.kxxcn.maru.util.extension.openDialog
import dev.kxxcn.maru.util.extension.setupSnackbar
import javax.inject.Inject

class BackupFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<BackupViewModel> { viewModelFactory }

    private lateinit var binding: BackupFragmentBinding

    private var alertDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BackupFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@BackupFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupSnackbar()
        setupListener()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupListener() {
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().popBackStack()
        })
        viewModel.askEvent.observe(viewLifecycleOwner, EventObserver {
            showDialog(it)
        })
    }

    private fun showDialog(filterType: BackupFilterType) {
        when (filterType) {
            BackupFilterType.BACKUP -> R.drawable.ic_backup to R.string.backup_current_data
            BackupFilterType.RESTORE -> R.drawable.ic_restore to R.string.backup_overwrite_data
        }.also { (iconRes, testRes) ->
            alertDialog?.dismiss()
            alertDialog = openDialog(
                iconRes,
                getString(testRes),
                negative = { handleNegativeClick() },
                positive = { handlePositiveClick(filterType) }
            )
        }
    }

    private fun handleNegativeClick() {
        alertDialog?.dismiss()
    }

    private fun handlePositiveClick(filterType: BackupFilterType) {
        alertDialog?.dismiss()
        when (filterType) {
            BackupFilterType.BACKUP -> viewModel.backup()
            BackupFilterType.RESTORE -> viewModel.restore()
        }
    }
}
