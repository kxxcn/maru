package dev.kxxcn.maru.view.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.databinding.PurchaseFragmentBinding
import dev.kxxcn.maru.util.BillingManager
import dev.kxxcn.maru.util.extension.setupSnackbar
import dev.kxxcn.maru.view.base.BaseFragment
import javax.inject.Inject

class PurchaseFragment : BaseFragment() {

    @Inject
    lateinit var billingManager: BillingManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: PurchaseFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<PurchaseViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PurchaseFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@PurchaseFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupSnackbar()
        setupListener()
    }

    override fun onDestroyView() {
        billingManager.release()
        super.onDestroyView()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarRes)
    }

    private fun setupListener() {
        viewModel.closeEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().popBackStack()
        })
        viewModel.purchaseEvent.observe(viewLifecycleOwner, EventObserver {
            purchase()
        })
        viewModel.successEvent.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
        })
        viewModel.failureEvent.observe(viewLifecycleOwner, EventObserver {
            viewModel.handlePurchaseFailure()
        })
    }

    private fun purchase() {
        billingManager.purchasePremium(
            activity,
            { viewModel.handlePurchaseSuccess(it) },
            { viewModel.handlePurchaseFailure() }
        )
    }
}
