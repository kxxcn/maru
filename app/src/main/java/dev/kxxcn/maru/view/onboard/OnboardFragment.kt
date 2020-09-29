package dev.kxxcn.maru.view.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.OnboardFragmentBinding
import dev.kxxcn.maru.view.onboard.page.OnboardPagerAdapter

class OnboardFragment : Fragment(R.layout.onboard_fragment) {

    private val viewModel by viewModels<OnboardViewModel>()

    private lateinit var binding: OnboardFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OnboardFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@OnboardFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupPagerAdapter()
        setupBackPressed()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupPagerAdapter() {
        binding.onboardPager.adapter = OnboardPagerAdapter(childFragmentManager)
        binding.dotsIndicator.setViewPager(binding.onboardPager)
    }

    private fun setupBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.onboardPager.currentItem != 0) {
                viewModel.onPrevious()
            }
        }

        viewModel.popBackStack.observe(viewLifecycleOwner, { findNavController().popBackStack() })
    }
}
