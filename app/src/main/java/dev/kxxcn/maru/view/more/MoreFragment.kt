package dev.kxxcn.maru.view.more

import dev.kxxcn.maru.util.AdSlotBinder
import dev.kxxcn.maru.util.AdHelper
import dev.kxxcn.maru.GlideApp
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.AdRequest
import androidx.core.view.isVisible
import androidx.appcompat.app.AppCompatDelegate
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.play.core.review.ReviewManager
import dev.kxxcn.maru.BuildConfig
import dev.kxxcn.maru.EventObserver
import dev.kxxcn.maru.MaruActivity
import dev.kxxcn.maru.R
import dev.kxxcn.maru.databinding.MoreFragmentBinding
import dev.kxxcn.maru.util.REQUEST_CODE_PERMISSION_LOCATION
import dev.kxxcn.maru.util.extension.openDialog
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.signin.SignInFragment
import javax.inject.Inject

class MoreFragment : SignInFragment() {

    @Inject
    lateinit var manager: ReviewManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: MoreFragmentBinding

    override val clazz: Class<*>
        get() = this::class.java

    override val viewModel by viewModels<MoreViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MoreFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@MoreFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLifecycle()
        setupListener()
        setupAd()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSION_LOCATION -> nightDialog()
        }
    }

    override fun onDestroyView() {
        releaseAd()
        super.onDestroyView()
    }

    override fun handleSignInSuccess() {
        viewModel.handleSignInSuccess()
    }

    override fun handleSignInFailure() {
        viewModel.handleSignInFailure()
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupListener() {
        viewModel.settingEvent.observe(viewLifecycleOwner, EventObserver {
            MoreFragmentDirections.actionMoreFragmentToSettingFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.contactEvent.observe(viewLifecycleOwner, EventObserver {
            contactDialog()
        })
        viewModel.noticeEvent.observe(viewLifecycleOwner, EventObserver {
            MoreFragmentDirections.actionMoreFragmentToNoticeFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.storeEvent.observe(viewLifecycleOwner, EventObserver {
            storeDialog()
        })
        viewModel.orderEvent.observe(viewLifecycleOwner, EventObserver {
            MoreFragmentDirections.actionMoreFragmentToOrderFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.adEvent.observe(viewLifecycleOwner, EventObserver {
            if (isSignedIn()) {
                viewModel.checkPremium(auth.currentUser?.email, forBackup = false)
            } else {
                signInDialog()
            }
        })
        viewModel.backupEvent.observe(viewLifecycleOwner, EventObserver {
            if (isSignedIn()) {
                viewModel.checkPremium(auth.currentUser?.email, forBackup = true)
            } else {
                signInDialog()
            }
        })
        viewModel.nightEvent.observe(viewLifecycleOwner, EventObserver { enabled ->
            applyNight(enabled)
        })
        viewModel.daysEvent.observe(viewLifecycleOwner, EventObserver {
            MoreFragmentDirections.actionMoreFragmentToDayFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.timelineEvent.observe(viewLifecycleOwner, EventObserver {
            MoreFragmentDirections.actionMoreFragmentToTimelineFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.presentEvent.observe(viewLifecycleOwner, EventObserver { filterType ->
            MoreFragmentDirections.actionMoreFragmentToPresentFragment(filterType).also {
                findNavController().navigate(it)
            }
        })
        viewModel.landmarkEvent.observe(viewLifecycleOwner, EventObserver {
            MoreFragmentDirections.actionMoreFragmentToLandmarkFragment().also {
                findNavController().navigate(it)
            }
        })
        viewModel.purchaseEvent.observe(viewLifecycleOwner, EventObserver {
            openPurchaseScreen()
        })
        viewModel.premiumEvent.observe(viewLifecycleOwner, EventObserver {
            openBackupScreen()
        })
    }

    private fun openStore() {
        alertDialog?.dismiss()
        openStore(context?.packageName)
    }

    private fun sendEmail() {
        startActivity(Intent(Intent.ACTION_SEND).apply {
            type = "plain/Text"
            val emails = arrayOf(getString(R.string.email_address))
            putExtra(Intent.EXTRA_EMAIL, emails)
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            putExtra(
                Intent.EXTRA_TEXT, getString(
                    R.string.email_text,
                    Build.DEVICE,
                    Build.VERSION.SDK_INT,
                    BuildConfig.VERSION_NAME
                )
            )
            type = "message/rfc822"
        })
    }

    private fun switch() {
        applyNight(!PreferenceUtils.useDarkMode)
    }

    private fun applyNight(useDarkMode: Boolean) {
        if (PreferenceUtils.useDarkMode == useDarkMode) return
        PreferenceUtils.useDarkMode = useDarkMode
        AppCompatDelegate.setDefaultNightMode(
            if (useDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private var nativeAd: NativeAd? = null

    /** 더보기 하단 미디어형 광고 슬롯. 프리미엄이면 싣지 않는다. */
    private fun setupAd() {
        viewModel.isPremium.observe(viewLifecycleOwner) { premium ->
            if (premium) {
                releaseAd()
                binding.moreNativeAdContainer.isVisible = false
            } else if (nativeAd == null) {
                loadAd()
            }
        }
    }

    private fun loadAd() {
        val context = context ?: return
        val container = binding.moreNativeAdContainer
        val adHelper = AdHelper(context)
        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(VideoOptions.Builder().setStartMuted(true).build())
            .build()
        adHelper.createNativeAd(
            getString(R.string.admob_native_more_id),
            onFailed = { container.isVisible = false }
        ).forNativeAd { ad ->
            if (!isAdded) {
                ad.destroy()
                return@forNativeAd
            }
            val adView = LayoutInflater.from(context)
                .inflate(R.layout.ad_slot_view, container, false) as? NativeAdView
                ?: return@forNativeAd
            nativeAd?.destroy()
            nativeAd = ad
            AdSlotBinder.bind(adView, ad, GlideApp.with(this), showMedia = true)
            container.removeAllViews()
            container.addView(adView)
            container.isVisible = true
        }.withNativeAdOptions(adOptions).build().loadAd(AdRequest.Builder().build())
    }

    private fun releaseAd() {
        nativeAd?.destroy()
        nativeAd = null
        if (::binding.isInitialized) {
            binding.moreNativeAdContainer.removeAllViews()
        }
    }

    private fun contactDialog() {
        alertDialog?.dismiss()
        alertDialog = openDialog(
            R.drawable.ic_email,
            getString(R.string.send_email),
            negative = { handleNegativeSelection() },
            positive = { handleContactSelection() }
        )
    }

    private fun storeDialog() {
        alertDialog?.dismiss()
        alertDialog = openDialog(
            R.drawable.ic_review,
            getString(R.string.write_review),
            negative = { handleNegativeSelection() },
            positive = { openStore() }
        )
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

    private fun nightDialog() {
        val text = if (PreferenceUtils.useDarkMode) {
            getString(R.string.inactive_night_mode)
        } else {
            getString(R.string.active_night_mode)
        }
        alertDialog?.dismiss()
        alertDialog = openDialog(
            R.drawable.ic_moon,
            text,
            negative = { handleNegativeSelection() },
            positive = { handleSwitchSelection() }
        )
    }

    private fun handleNegativeSelection() {
        alertDialog?.dismiss()
    }

    private fun handleContactSelection() {
        alertDialog?.dismiss()
        sendEmail()
    }

    private fun handleSignInSelection() {
        alertDialog?.dismiss()
        signIn()
    }

    private fun handleSwitchSelection() {
        alertDialog?.dismiss()
        switch()
    }

    private fun openPurchaseScreen() {
        MoreFragmentDirections.actionMoreFragmentToPurchaseFragment().also {
            findNavController().navigate(it)
        }
    }

    private fun openBackupScreen() {
        MoreFragmentDirections.actionMoreFragmentToBackupFragment().also {
            findNavController().navigate(it)
        }
    }
}
