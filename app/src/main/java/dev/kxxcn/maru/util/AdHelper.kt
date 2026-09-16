package dev.kxxcn.maru.util

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.AdHelper.AdMobFilterType.*
import dev.kxxcn.maru.util.extension.or

class AdHelper(private val context: Context) : LifecycleObserver {

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
        return AdView(context).apply {
            adUnitId = getAdUnitId(BANNER, id)
            size?.let(::setAdSize)
            adListener = createAdListener(BANNER, adUnitId)
        }
    }

    fun createNativeAd(id: String): AdLoader.Builder {
        val adUnitId = getAdUnitId(NATIVE, id)
        return AdLoader.Builder(context, adUnitId)
            .withAdListener(createAdListener(NATIVE, adUnitId))
    }

    private fun createAdListener(type: AdMobFilterType, id: String) = object : AdListener() {
        override fun onAdLoaded() {
            Log.d("AdMob", "$type loaded: $id")
        }

        override fun onAdFailedToLoad(error: LoadAdError) {
            Log.w("AdMob", "$type failed to load: $id\n$error")
        }
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
                                Log.w("AdMob", "INTERSTITIAL failed to show: $id\n$adError")
                                interstitialAd = null
                                onFailedToShow()
                            }
                        }
                    }
                    onLoaded()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.w("AdMob", "INTERSTITIAL failed to load: ${getAdUnitId(INTERSTITIAL, id)}\n$loadAdError")
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
