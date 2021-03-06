package dev.kxxcn.maru.view.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dev.kxxcn.maru.databinding.TimelineFragmentBinding
import dev.kxxcn.maru.util.LinearSpacingDecoration
import dev.kxxcn.maru.util.extension.px
import dev.kxxcn.maru.view.base.BaseFragment

class TimelineFragment : BaseFragment() {

    private lateinit var binding: TimelineFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<TimelineViewModel>()

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
}
