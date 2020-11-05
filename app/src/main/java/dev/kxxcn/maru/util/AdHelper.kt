package dev.kxxcn.maru.util

import android.content.Context
import androidx.lifecycle.LifecycleObserver
import com.google.android.gms.ads.*
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
        get() = interstitialAd?.isLoaded == true

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
            adSize = size
        }.also { adViewRef = WeakReference(it) }
    }

    fun createNativeAd(id: String): AdLoader.Builder {
        return adBuilderRef?.get() ?: AdLoader.Builder(
            context,
            getAdUnitId(NATIVE, id)
        ).also { adBuilderRef = WeakReference(it) }
    }

    fun loadInterstitialAd(id: String, listener: AdListener) {
        interstitialAd ?: InterstitialAd(context).apply {
            adUnitId = getAdUnitId(INTERSTITIAL, id)
            adListener = listener
        }.also { ad ->
            interstitialAd = ad
            ad.loadAd(AdRequest.Builder().build())
        }
    }

    fun request() {
        refCount++
    }

    fun show() {
        interstitialAd?.show()
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
