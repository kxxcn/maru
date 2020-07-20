package dev.kxxcn.maru.view.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.OrderFragmentBinding
import dev.kxxcn.maru.util.LinearSpacingDecoration
import dev.kxxcn.maru.util.extension.px
import javax.inject.Inject

class OrderFragment : Fragment(R.layout.order_fragment) {

    private val viewModel by viewModels<OrderViewModel>()

    private lateinit var binding: OrderFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OrderFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@OrderFragment.viewModel
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
        binding.orderList.adapter = null
        super.onDestroyView()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListAdapter() {
        with(binding.orderList) {
            addItemDecoration(LinearSpacingDecoration(50.px), 0)
            adapter = OrderAdapter()
        }
    }

    private fun setupListener() {
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().popBackStack()
        })
    }
}
