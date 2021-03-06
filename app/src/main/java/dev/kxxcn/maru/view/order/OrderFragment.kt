package dev.kxxcn.maru.view.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dev.kxxcn.maru.databinding.OrderFragmentBinding
import dev.kxxcn.maru.util.LinearSpacingDecoration
import dev.kxxcn.maru.util.extension.px
import dev.kxxcn.maru.view.base.BaseFragment

class OrderFragment : BaseFragment() {

    private lateinit var binding: OrderFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<OrderViewModel>()

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
}
