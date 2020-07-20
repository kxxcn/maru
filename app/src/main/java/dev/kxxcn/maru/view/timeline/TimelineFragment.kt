package dev.kxxcn.maru.view.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.databinding.TimelineFragmentBinding
import dev.kxxcn.maru.util.LinearSpacingDecoration
import dev.kxxcn.maru.util.extension.px

class TimelineFragment : Fragment() {

    private val viewModel by viewModels<TimelineViewModel>()

    private lateinit var binding: TimelineFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TimelineFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@TimelineFragment.viewModel
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
        binding.timelineList.adapter = null
        super.onDestroyView()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel ?: return
        with(binding.timelineList) {
            addItemDecoration(LinearSpacingDecoration(20.px), 0)
            adapter = TimelineAdapter(viewModel)
        }
    }

    private fun setupListener() {
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().popBackStack()
        })
    }
}
