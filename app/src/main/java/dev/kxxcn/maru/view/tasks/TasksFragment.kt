package dev.kxxcn.maru.view.tasks

import dev.kxxcn.maru.R
import dev.kxxcn.maru.view.register.RegisterFilterType
import dev.kxxcn.maru.view.custom.SegmentView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.GlideApp
import dev.kxxcn.maru.databinding.TasksFragmentBinding
import dev.kxxcn.maru.view.base.BaseFragment
import dev.kxxcn.maru.view.base.Scrollable
import javax.inject.Inject

class TasksFragment : BaseFragment(), Scrollable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: TasksFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<TasksViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TasksFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@TasksFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupListAdapter()
        setupSegment()
        setupListener()
    }

    override fun onDestroyView() {
        if (::binding.isInitialized) {
            with(binding.tasksList) {
                clearOnScrollListeners()
                adapter = null
            }
        }
        super.onDestroyView()
    }

    override fun scrollToTop() {
        binding.tasksList.smoothScrollToPosition(0)
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel ?: return
        with(binding.tasksList) {
            adapter = TasksAdapter(viewModel, GlideApp.with(this@TasksFragment))
        }
    }

    private fun setupListener() {
        viewModel.taskSelectionEvent.observe(
            viewLifecycleOwner,
            EventObserver { (taskDetail, isPremium) ->
                TasksFragmentDirections.actionTasksFragmentToInputFragment(
                    taskDetail.task?.id,
                    isPremium
                ).also {
                    findNavController().navigate(it)
                }
            })
        viewModel.editEvent.observe(viewLifecycleOwner, EventObserver {
            TasksFragmentDirections.actionTasksFragmentToEditFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.addEvent.observe(viewLifecycleOwner, EventObserver {
            TasksFragmentDirections.actionTasksFragmentToEditDialogFragment(
                RegisterFilterType.REGISTER_TASK
            ).also {
                findNavController().navigate(it)
            }
        })
    }

    private fun setupSegment() {
        with(binding.tasksSegment) {
            setSegments(
                listOf(
                    SegmentView.Segment(getString(R.string.tasks_total_desc)),
                    SegmentView.Segment(getString(R.string.tasks_progress_desc)),
                    SegmentView.Segment(getString(R.string.tasks_completed_desc))
                )
            )
            onSelected = { index -> viewModel.setFiltering(TasksFilterType.values()[index]) }
        }
        viewModel.filterType.observe(viewLifecycleOwner) {
            binding.tasksSegment.selectedIndex = it.ordinal
        }
        viewModel.totalCount.observe(viewLifecycleOwner) { updateCounts() }
        viewModel.activeCount.observe(viewLifecycleOwner) { updateCounts() }
        viewModel.completedCount.observe(viewLifecycleOwner) { updateCounts() }
    }

    private fun updateCounts() {
        binding.tasksSegment.setCounts(
            listOf(viewModel.totalCount.value, viewModel.activeCount.value, viewModel.completedCount.value)
        )
    }

}
