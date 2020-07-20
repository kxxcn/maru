package dev.kxxcn.maru.view.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.InputFragmentBinding
import dev.kxxcn.maru.util.AdHelper
import dev.kxxcn.maru.util.KEY_TASK_ID
import dev.kxxcn.maru.util.extension.setupSnackbar
import javax.inject.Inject

class InputFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<InputViewModel> { viewModelFactory }

    private lateinit var binding: InputFragmentBinding

    private lateinit var interstitialAd: InterstitialAd

    private var adRequestCount = 0

    private var adHelper = AdHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InputFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@InputFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupArguments()
        setupSnackbar()
        setupListener()
        setupInterstitial()
    }

    override fun onDestroyView() {
        adHelper.release()
        super.onDestroyView()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupArguments() {
        viewModel.start(arguments?.getString(KEY_TASK_ID))
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupListener() {
        viewModel.closeEvent.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })
        viewModel.completeEvent.observe(viewLifecycleOwner, EventObserver {
            showAd()
        })
    }

    private fun setupInterstitial() {
        val context = context ?: return
        val id = getString(R.string.admob_interstitial_task_id)
        interstitialAd = adHelper.loadInterstitialAd(context, id, object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                if (adRequestCount == 0) return
                interstitialAd.show()
            }

            override fun onAdOpened() {
                super.onAdOpened()
                openStatusFragment()
            }
        })
    }

    private fun showAd() {
        try {
            adRequestCount++
            if (interstitialAd.isLoading) return
            interstitialAd.show()
        } catch (e: Exception) {
            openStatusFragment()
        }
    }

    private fun openStatusFragment() {
        InputFragmentDirections.actionInputFragmentToStatusFragment().also {
            findNavController().navigate(it)
        }
    }
}
