package dev.kxxcn.maru.view.present

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.databinding.PresentFragmentBinding
import dev.kxxcn.maru.util.PAGER_CATEGORY
import dev.kxxcn.maru.view.base.BaseFragment

class PresentFragment : BaseFragment() {

    private lateinit var binding: PresentFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<PresentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PresentFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@PresentFragment.viewModel
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
        binding.presentPager.adapter = PresentPagerAdapter(childFragmentManager)
    }

    private fun setupBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.presentPager.currentItem == PAGER_CATEGORY) {
                findNavController().popBackStack()
            } else {
                viewModel.onBackPressed()
            }
        }
    }
}
