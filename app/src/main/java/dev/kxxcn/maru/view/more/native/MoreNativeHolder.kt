package dev.kxxcn.maru.view.more.native

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.AdHelper
import dev.kxxcn.maru.util.extension.asTextView

class MoreNativeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var currentNativeAd: NativeAd? = null

    private val container: FrameLayout = itemView.findViewById(R.id.native_ad_container)

    private val adHelper = AdHelper(itemView.context)

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
            context.getString(R.string.admob_native_more_id)
        ).forNativeAd { nativeAd ->
            val inflater = LayoutInflater.from(context)
            val adView = inflater.inflate(R.layout.more_native_view, null)
                    as? NativeAdView
                ?: return@forNativeAd
            populateNativeAdView(nativeAd, adView)
            container.removeAllViews()
            container.addView(adView)
        }.withNativeAdOptions(adOptions).build().also { adLoader ->
            adLoader.loadAd(AdRequest.Builder().build())
        }.run { { release() } }
    }

    private fun populateNativeAdView(
        nativeAd: NativeAd,
        adView: NativeAdView
    ) {
        currentNativeAd?.destroy()
        currentNativeAd = nativeAd

        with(adView) {
            val media = findViewById<MediaView>(R.id.ad_media)
            val headline = findViewById<View>(R.id.ad_headline)
            val callToAction = findViewById<View>(R.id.ad_call_to_action)

            mediaView = media
            headlineView = headline
            callToActionView = callToAction

            media.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

            headline.asTextView().text = nativeAd.headline
            callToAction.asTextView().text =
                nativeAd.callToAction ?: context.getString(R.string.menu_more)

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
