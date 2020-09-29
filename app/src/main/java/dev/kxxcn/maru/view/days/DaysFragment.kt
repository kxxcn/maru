package dev.kxxcn.maru.view.days

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.data.Day
import dev.kxxcn.maru.databinding.DaysFragmentBinding
import dev.kxxcn.maru.util.DayGridSpacingDecoration
import dev.kxxcn.maru.util.extension.openDialog
import dev.kxxcn.maru.view.base.BaseFragment
import javax.inject.Inject

class DaysFragment : BaseFragment() {

    override val clazz: Class<*>
        get() = this::class.java

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: DaysFragmentBinding

    private var alertDialog: AlertDialog? = null

    override val viewModel by viewModels<DaysViewModel> { viewModelFactory }

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

    private fun setupListener() {
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
        viewModel.isEmpty.observe(viewLifecycleOwner, { binding.daysEmpty.isVisible = it })
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
