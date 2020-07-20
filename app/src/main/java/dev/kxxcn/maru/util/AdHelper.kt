package dev.kxxcn.maru.util

import android.content.Context
import com.google.android.gms.ads.*
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.AdHelper.AdMobFilterType.*
import dev.kxxcn.maru.util.extension.or
import java.lang.ref.WeakReference

class AdHelper {

    private var adViewRef: WeakReference<AdView>? = null

    private var adBuilderRef: WeakReference<AdLoader.Builder>? = null

    private var interstitialAdRef: WeakReference<InterstitialAd>? = null

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

    fun createBannerAd(context: Context, id: String, size: AdSize?): AdView {
        return adViewRef?.get() ?: AdView(context).apply {
            adUnitId = getAdUnitId(context, BANNER, id)
            adSize = size
        }.also { adViewRef = WeakReference(it) }
    }

    fun createNativeAd(context: Context, id: String): AdLoader.Builder {
        return adBuilderRef?.get() ?: AdLoader.Builder(
            context,
            getAdUnitId(context, NATIVE, id)
        ).also { adBuilderRef = WeakReference(it) }
    }

    fun loadInterstitialAd(context: Context, id: String, listener: AdListener): InterstitialAd {
        return interstitialAdRef?.get() ?: InterstitialAd(context).apply {
            adUnitId = getAdUnitId(context, INTERSTITIAL, id)
            adListener = listener
        }.also { ad ->
            interstitialAdRef = WeakReference(ad)
            ad.loadAd(AdRequest.Builder().build())
        }
    }

    fun release() {
        interstitialAdRef?.get()?.adListener = null
    }

    private fun getAdUnitId(context: Context, filterType: AdMobFilterType, id: String): String {
        return when (filterType) {
            BANNER -> id or context.getString(R.string.admob_banner_test_id)
            NATIVE -> id or context.getString(R.string.admob_native_test_id)
            INTERSTITIAL -> id or context.getString(R.string.admob_interstitial_test_id)
        }
    }
}
