package dev.kxxcn.maru.view.days

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Day
import dev.kxxcn.maru.databinding.DaysFragmentBinding
import dev.kxxcn.maru.util.DayGridSpacingDecoration
import dev.kxxcn.maru.util.extension.openDialog
import dev.kxxcn.maru.util.extension.setupSnackbar
import javax.inject.Inject

class DaysFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<DaysViewModel> { viewModelFactory }

    private lateinit var binding: DaysFragmentBinding

    private var alertDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DaysFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@DaysFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupSnackbar()
        setupListener()
        setupListAdapter()
    }

    override fun onDestroyView() {
        alertDialog = null
        binding.daysList.adapter = null
        super.onDestroyView()
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
        viewModel.addEvent.observe(viewLifecycleOwner, EventObserver {
            DaysFragmentDirections.actionDaysFragmentToDaysAddFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.deleteEvent.observe(viewLifecycleOwner, EventObserver {
            alertDialog?.dismiss()
            alertDialog = openDialog(
                R.drawable.ic_delete,
                getString(R.string.days_deletion_message, it.content),
                negative = { handleNegativeClick() },
                positive = { handlePositiveClick(it) }
            )
        })
        viewModel.isEmpty.observe(viewLifecycleOwner, Observer {
            binding.daysEmpty.isVisible = it
        })
    }

    private fun setupListAdapter() {
        with(binding.daysList) {
            addItemDecoration(DayGridSpacingDecoration())
            adapter = DaysAdapter {
                viewModel.delete(it)
            }
        }
    }

    private fun handleNegativeClick() {
        alertDialog?.dismiss()
    }

    private fun handlePositiveClick(day: Day) {
        alertDialog?.dismiss()
        viewModel.handleDeletionSelection(day)
    }
}
