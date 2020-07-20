package dev.kxxcn.maru.view.present.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.databinding.CategoryFragmentBinding
import dev.kxxcn.maru.util.LinearSpacingDecoration
import dev.kxxcn.maru.util.extension.displayWidth
import dev.kxxcn.maru.view.present.PresentViewModel

class CategoryFragment : Fragment() {

    private val viewModel by lazy {
        parentFragment?.let {
            ViewModelProvider(it).get(PresentViewModel::class.java)
        } ?: throw Exception("Invalid Fragment.")
    }

    private lateinit var binding: CategoryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CategoryFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@CategoryFragment.viewModel
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
        binding.categoryList.adapter = null
        super.onDestroyView()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel ?: return
        with(binding.categoryList) {
            addItemDecoration(LinearSpacingDecoration(), 0)
            adapter = PresentAdapter(viewModel, displayWidth() / 2)
        }
    }

    private fun setupListener() {
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().popBackStack()
        })
    }
}
