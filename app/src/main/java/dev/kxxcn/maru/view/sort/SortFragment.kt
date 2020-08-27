package dev.kxxcn.maru.view.sort

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.SortFragmentBinding
import dev.kxxcn.maru.di.MaruSavedStateViewModelFactory
import dev.kxxcn.maru.util.extension.openDialog
import dev.kxxcn.maru.util.extension.setupSnackbar
import dev.kxxcn.maru.view.base.BaseDaggerFragment
import javax.inject.Inject

class SortFragment : BaseDaggerFragment() {

    override val clazz: Class<*>
        get() = this::class.java

    @Inject
    lateinit var viewModelFactory: MaruSavedStateViewModelFactory

    private val viewModel by viewModels<SortViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    private lateinit var binding: SortFragmentBinding

    private var alertDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SortFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@SortFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupListAdapter()
        setupSnackbar()
        setupListener()
        setupOnBackPressed()
    }

    override fun onDestroyView() {
        alertDialog = null
        binding.tasksList.adapter = null
        super.onDestroyView()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel ?: return
        binding.tasksList.adapter = SortAdapter(viewModel)
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText)
    }

    private fun setupListener() {
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().popBackStack()
        })
        viewModel.deleteEvent.observe(viewLifecycleOwner, EventObserver {
            showDialog(it)
        })
        viewModel.toastText.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.saveTasks()
        }
    }

    private fun showDialog(count: Int) {
        alertDialog?.dismiss()
        alertDialog = openDialog(
            R.drawable.ic_delete,
            getString(R.string.sort_tasks_deletion_dialog_message, count),
            negative = { handleNegativeClick() },
            positive = { handlePositiveClick() }
        )
    }

    private fun handleNegativeClick() {
        alertDialog?.dismiss()
    }

    private fun handlePositiveClick() {
        alertDialog?.dismiss()
        viewModel.deleteTasks()
    }
}
