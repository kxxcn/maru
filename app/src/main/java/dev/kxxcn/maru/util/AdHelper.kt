package dev.kxxcn.maru.util

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.AdHelper.AdMobFilterType.*
import dev.kxxcn.maru.util.extension.or
import java.lang.ref.WeakReference

class AdHelper(private val context: Context) : LifecycleObserver {

    private var adViewRef: WeakReference<AdView>? = null

    private var adBuilderRef: WeakReference<AdLoader.Builder>? = null

    private var interstitialAd: InterstitialAd? = null

    private var refCount = 0

    val isRequested: Boolean
        get() = refCount > 0

    val isLoaded: Boolean
        get() = interstitialAd != null

    private fun getAdUnitId(filterType: AdMobFilterType, id: String): String {
        return when (filterType) {
            BANNER -> id or context.getString(R.string.admob_banner_test_id)
            NATIVE -> id or context.getString(R.string.admob_native_test_id)
            INTERSTITIAL -> id or context.getString(R.string.admob_interstitial_test_id)
        }
    }

    fun createBannerAd(id: String, size: AdSize?): AdView {
        return adViewRef?.get() ?: AdView(context).apply {
            adUnitId = getAdUnitId(BANNER, id)
            size?.let(::setAdSize)
        }.also { adViewRef = WeakReference(it) }
    }

    fun createNativeAd(id: String): AdLoader.Builder {
        return adBuilderRef?.get() ?: AdLoader.Builder(
            context,
            getAdUnitId(NATIVE, id)
        ).also { adBuilderRef = WeakReference(it) }
    }

    fun loadInterstitialAd(
        id: String,
        onLoaded: () -> Unit = {},
        onShowed: () -> Unit = {},
        onDismissed: () -> Unit = {},
        onFailedToShow: () -> Unit = {}
    ) {
        if (interstitialAd != null) return

        InterstitialAd.load(
            context,
            getAdUnitId(INTERSTITIAL, id),
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad.apply {
                        fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdShowedFullScreenContent() {
                                onShowed()
                            }

                            override fun onAdDismissedFullScreenContent() {
                                interstitialAd = null
                                onDismissed()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                interstitialAd = null
                                onFailedToShow()
                            }
                        }
                    }
                    onLoaded()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    fun request() {
        refCount++
    }

    fun show(activity: Activity) {
        interstitialAd?.show(activity)
    }

    enum class AdMobFilterType {

        /**
         * 배너 광고
         */
        BANNER,

        /**
         * 네이티브 광고
         */
        NATIVE,

        /**
         * 전면 광고
         */
        INTERSTITIAL
    }
}
