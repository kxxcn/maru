package dev.kxxcn.maru.view.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import dev.kxxcn.maru.R
import javax.inject.Inject

class SplashFragment : DaggerFragment(R.layout.splash_fragment) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<SplashViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {
        viewModel.register.observe(viewLifecycleOwner, Observer {
            navigate(it)
        })
    }

    private fun navigate(loggedIn: Boolean) {
        if (loggedIn) {
            SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        } else {
            SplashFragmentDirections.actionSplashFragmentToIntroFragment()
        }.also {
            findNavController().navigate(it)
        }
    }
}
