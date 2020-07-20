package dev.kxxcn.maru.view.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.EditFragmentBinding
import dev.kxxcn.maru.util.KEY_REGISTER_TYPE
import dev.kxxcn.maru.view.register.RegisterFilterType
import javax.inject.Inject

class EditFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<EditViewModel> { viewModelFactory }

    private lateinit var binding: EditFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EditFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@EditFragment.viewModel
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
        viewModel.editEvent.observe(viewLifecycleOwner, EventObserver {
            openEditDialog(it)
        })
    }

    private fun openEditDialog(filterType: RegisterFilterType) {
        findNavController().navigate(
            R.id.edit_dialog_fragment,
            bundleOf(KEY_REGISTER_TYPE to filterType)
        )
    }
}
