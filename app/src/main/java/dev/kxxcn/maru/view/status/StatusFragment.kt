package dev.kxxcn.maru.view.status

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.databinding.StatusFragmentBinding
import dev.kxxcn.maru.view.base.BaseFragment
import javax.inject.Inject

class StatusFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: StatusFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<StatusViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StatusFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@StatusFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupListener()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListener() {
        viewModel.motionEvent.observe(viewLifecycleOwner, EventObserver {
            binding.statusMotion.transitionToEnd()
        })
    }
}
