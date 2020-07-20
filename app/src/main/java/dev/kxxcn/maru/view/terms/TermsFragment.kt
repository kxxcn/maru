package dev.kxxcn.maru.view.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.databinding.TermsFragmentBinding

class TermsFragment : Fragment() {

    private val viewModel by viewModels<TermsViewModel>()

    private lateinit var binding: TermsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TermsFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@TermsFragment.viewModel
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
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().popBackStack()
        })
    }
}
