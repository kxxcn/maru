package dev.kxxcn.maru.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.maru.MaruActivity
import dev.kxxcn.maru.databinding.HomeFragmentBinding
import dev.kxxcn.maru.util.LinearSpacingDecoration
import dev.kxxcn.maru.util.extension.setupSnackbar
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.BaseDaggerFragment
import javax.inject.Inject

class HomeFragment : BaseDaggerFragment() {

    override val clazz: Class<*>
        get() = this::class.java

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }

    private lateinit var binding: HomeFragmentBinding

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
        setupSnackbar()
        setupOnboard()
        setupListAdapter()
        setupOnBackPressed()
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

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText)
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
                addItemDecoration(LinearSpacingDecoration(), 0)
                val activity = activity ?: return
                adapter = HomeAdapter(activity, viewModel)
            }
        }
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

        }
    }

    fun navigate(pos: Int) {

    }
}
