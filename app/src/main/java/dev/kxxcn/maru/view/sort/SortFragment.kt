package dev.kxxcn.maru.view.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.SortFragmentBinding
import dev.kxxcn.maru.di.MaruSavedStateViewModelFactory
import dev.kxxcn.maru.util.extension.openDialog
import dev.kxxcn.maru.view.base.BaseFragment
import dev.kxxcn.maru.view.register.RegisterFilterType
import javax.inject.Inject

class SortFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: MaruSavedStateViewModelFactory

    private lateinit var binding: SortFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<SortViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        setupListener()
        setupOnBackPressed()
    }

    override fun onDestroyView() {
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

    private fun setupListener() {
        viewModel.deleteEvent.observe(viewLifecycleOwner, EventObserver {
            showDialog(it)
        })
        viewModel.addTaskEvent.observe(viewLifecycleOwner, EventObserver {
            addTask()
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

    private fun addTask() {
        SortFragmentDirections.actionSortFragmentToEditDialogFragment(RegisterFilterType.REGISTER_TASK)
            .also { findNavController().navigate(it) }
    }
}
