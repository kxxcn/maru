package dev.kxxcn.maru.view.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.MaruActivity
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.IntroFragmentBinding
import dev.kxxcn.maru.util.NAV_HOME
import dev.kxxcn.maru.util.extension.openDialog
import dev.kxxcn.maru.util.extension.setTransitionCompleteListener
import dev.kxxcn.maru.view.signin.SignInFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class IntroFragment : SignInFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<IntroViewModel> { viewModelFactory }

    private lateinit var binding: IntroFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = IntroFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@IntroFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupMotionLayout()
        setupListener()
    }

    override fun handleSignInSuccess() {
        IntroFragmentDirections.actionIntroFragmentToBackupFragment().also {
            findNavController().navigate(it)
        }
    }

    override fun handleSignInFailure() {
        viewModel.handleSignInFailure()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupMotionLayout() {
        binding.introMotion.apply {
            setTransitionCompleteListener { _, _ ->
                IntroFragmentDirections.actionIntroFragmentToRegisterFragment().also {
                    findNavController().navigate(it)
                }
            }
        }
    }

    private fun setupListener() {
        viewModel.hasProfile.observe(viewLifecycleOwner, {
            if (it) (activity as? MaruActivity)?.navigate(NAV_HOME)
        })
        viewModel.startEvent.observe(viewLifecycleOwner, EventObserver {
            start()
        })
        viewModel.signInEvent.observe(viewLifecycleOwner, EventObserver {
            if (isSignedIn()) {
                handleSignInSelection()
            } else {
                signInDialog()
            }
        })
    }

    private fun start() {
        binding.introMotion.transitionToEnd()
    }

    private fun signInDialog() {
        alertDialog?.dismiss()
        alertDialog = openDialog(
            R.drawable.ic_sign_in,
            getString(R.string.need_sign_in_process),
            negative = { handleNegativeSelection() },
            positive = { handleSignInSelection() }
        )
    }

    private fun handleNegativeSelection() {
        alertDialog?.dismiss()
    }

    private fun handleSignInSelection() {
        alertDialog?.dismiss()
        signIn()
    }
}
