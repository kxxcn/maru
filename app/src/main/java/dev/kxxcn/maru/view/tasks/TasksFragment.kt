package dev.kxxcn.maru.view.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerFragment
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.GlideApp
import dev.kxxcn.maru.MaruActivity
import dev.kxxcn.maru.databinding.TasksFragmentBinding
import dev.kxxcn.maru.util.LinearSpacingDecoration
import dev.kxxcn.maru.util.extension.px
import javax.inject.Inject

class TasksFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<TasksViewModel> { viewModelFactory }

    private lateinit var binding: TasksFragmentBinding

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

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel ?: return
        with(binding.tasksList) {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy == 0) return
                    showNavigator(dy < 0)
                }
            })
            addItemDecoration(LinearSpacingDecoration(size = 20.px), 0)
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
        viewModel.navigateEvent.observe(viewLifecycleOwner, EventObserver {
            showNavigator()
        })
        viewModel.editEvent.observe(viewLifecycleOwner, EventObserver {
            TasksFragmentDirections.actionTasksFragmentToEditFragment().also {
                findNavController().navigate(it)
            }
        })
    }

    private fun showNavigator(isShowing: Boolean = true) {
        val activity = requireActivity() as MaruActivity
        activity.openNavigator(isShowing)
    }
}
