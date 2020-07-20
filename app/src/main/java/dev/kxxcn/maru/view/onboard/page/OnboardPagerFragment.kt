package dev.kxxcn.maru.view.onboard.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import dev.kxxcn.maru.databinding.OnboardPagerFragmentBinding
import dev.kxxcn.maru.util.KEY_ONBOARD_TYPE
import dev.kxxcn.maru.view.onboard.page.OnboardPagerFilterType.*
import javax.inject.Inject

class OnboardPagerFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<OnboardPagerViewModel> { viewModelFactory }

    private lateinit var binding: OnboardPagerFragmentBinding

    private lateinit var filterType: OnboardPagerFilterType

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OnboardPagerFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@OnboardPagerFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupArguments()
        setupFiltering()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupArguments() {
        filterType = when (arguments?.getInt(KEY_ONBOARD_TYPE)) {
            0 -> ONBOARD_TASKS
            1 -> ONBOARD_ACCOUNT
            else -> ONBOARD_WELCOME
        }
    }

    private fun setupFiltering() {
        viewModel.setFiltering(filterType)
    }
}
