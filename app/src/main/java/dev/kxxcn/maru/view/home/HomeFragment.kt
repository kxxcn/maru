package dev.kxxcn.maru.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.MaruActivity
import dev.kxxcn.maru.databinding.HomeFragmentBinding
import dev.kxxcn.maru.util.LinearSpacingDecoration
import dev.kxxcn.maru.util.extension.px
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.BaseFragment
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: HomeFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@HomeFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupOnboard()
        setupListAdapter()
        setupListener()
    }

    override fun onDestroyView() {
        if (::binding.isInitialized) {
            with(binding.contentsList) {
                clearOnScrollListeners()
                adapter = null
            }
        }
        super.onDestroyView()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupOnboard() {
        if (!PreferenceUtils.shownOnboard) {
            HomeFragmentDirections.actionHomeFragmentToOnboardFragment().also {
                findNavController().navigate(it)
            }
            PreferenceUtils.shownOnboard = !PreferenceUtils.shownOnboard
        }
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel
        if (viewModel != null) {
            with(binding.contentsList) {
                layoutManager = LinearLayoutManager(context)
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy == 0) return
                        val activity = requireActivity() as MaruActivity
                        activity.openNavigator(dy < 0)
                    }
                })
                addItemDecoration(LinearSpacingDecoration(50.px), 0)
                val activity = activity ?: return
                adapter = HomeAdapter(activity, viewModel)
            }
        }
    }

    private fun setupListener() {
        viewModel.daysEvent.observe(viewLifecycleOwner, EventObserver {
            HomeFragmentDirections.actionHomeFragmentToDaysFragment().also {
                findNavController().navigate(it)
            }
        })
    }
}
