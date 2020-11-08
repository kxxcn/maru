package dev.kxxcn.maru.view.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.databinding.EditFragmentBinding
import dev.kxxcn.maru.view.base.BaseFragment
import dev.kxxcn.maru.view.register.RegisterFilterType
import javax.inject.Inject

class EditFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: EditFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<EditViewModel> { viewModelFactory }

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
        viewModel.editEvent.observe(viewLifecycleOwner, EventObserver {
            openEditDialog(it)
        })
    }

    private fun openEditDialog(filterType: RegisterFilterType) {
        EditFragmentDirections.actionEditFragmentToEditDialogFragment(filterType).also {
            findNavController().navigate(it)
        }
    }
}
