package dev.kxxcn.maru.view.more.native

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.AdHelper

class MoreNativeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var currentNativeAd: UnifiedNativeAd? = null

    private val container: FrameLayout = itemView.findViewById(R.id.native_ad_container)

    private val adHelper = AdHelper()

    @SuppressLint("InflateParams")
    fun loadAd(): () -> Unit {
        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()

        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()
        val context = itemView.context

        return adHelper.createNativeAd(
            context,
            context.getString(R.string.admob_native_more_id)
        ).forUnifiedNativeAd { unifiedNativeAd ->
            val inflater = LayoutInflater.from(context)
            val adView = inflater.inflate(R.layout.more_native_view, null)
                    as? UnifiedNativeAdView
                ?: return@forUnifiedNativeAd
            populateUnifiedNativeAdView(unifiedNativeAd, adView)
            container.removeAllViews()
            container.addView(adView)
        }.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
                container.removeAllViews()
            }
        }).withNativeAdOptions(adOptions).build().also { adLoader ->
            adLoader.loadAd(AdRequest.Builder().build())
        }.run { { release() } }
    }

    private fun populateUnifiedNativeAdView(
        nativeAd: UnifiedNativeAd,
        adView: UnifiedNativeAdView
    ) {
        currentNativeAd?.destroy()
        currentNativeAd = nativeAd

        with(adView) {
            mediaView = findViewById(R.id.ad_media)
            bodyView = findViewById(R.id.ad_body)
            callToActionView = findViewById(R.id.ad_call_to_action)

            mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            bodyView.visibility = if (nativeAd.body.isEmpty()) {
                View.GONE
            } else {
                (bodyView as? TextView)?.text = nativeAd.body
                View.VISIBLE
            }
            callToActionView.visibility = if (nativeAd.callToAction.isEmpty()) {
                View.GONE
            } else {
                (callToActionView as? TextView)?.text = nativeAd.callToAction
                View.VISIBLE
            }

            setNativeAd(nativeAd)
        }
    }

    private fun release() {
        currentNativeAd?.destroy()
        container.removeAllViews()
    }

    companion object {

        fun from(parent: ViewGroup): MoreNativeHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.more_native_item, parent, false)
            return MoreNativeHolder(view)
        }
    }
}
