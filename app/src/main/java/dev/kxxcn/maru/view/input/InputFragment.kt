package dev.kxxcn.maru.view.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdListener
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.InputFragmentBinding
import dev.kxxcn.maru.di.MaruSavedStateViewModelFactory
import dev.kxxcn.maru.util.AdHelper
import dev.kxxcn.maru.view.base.BaseFragment
import javax.inject.Inject

class InputFragment : BaseFragment() {

    @Inject
    lateinit var adHelper: AdHelper

    @Inject
    lateinit var viewModelFactory: MaruSavedStateViewModelFactory

    private lateinit var binding: InputFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<InputViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

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
        setupListener()
        setupInterstitial()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListener() {
        viewModel.doneEvent.observe(viewLifecycleOwner, EventObserver {
            openStatusFragment()
        })
        viewModel.adEvent.observe(viewLifecycleOwner, EventObserver {
            showAd()
        })
    }

    private fun setupInterstitial() {
        val id = getString(R.string.admob_interstitial_task_id)
        adHelper.loadInterstitialAd(id, object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                if (adHelper.isRequested) {
                    adHelper.show()
                }
            }

            override fun onAdOpened() {
                super.onAdOpened()
                openStatusFragment()
            }
        })
    }

    private fun showAd() {
        try {
            with(adHelper) {
                request()
                if (isLoaded) {
                    show()
                } else {
                    openStatusFragment()
                }
            }
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
