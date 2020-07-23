package dev.kxxcn.maru.view.tasks.holder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.AdHelper
import dev.kxxcn.maru.util.extension.asImageView
import dev.kxxcn.maru.util.extension.asTextView

class TasksNativeAdHolder(
    itemView: View,
    private val requestManager: RequestManager
) : RecyclerView.ViewHolder(itemView) {

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

        adHelper.createNativeAd(
            context,
            context.getString(R.string.admob_native_task_id)
        ).forUnifiedNativeAd { unifiedNativeAd ->
            val inflater = LayoutInflater.from(context)
            val adView = inflater.inflate(R.layout.tasks_native_view, null)
                    as? UnifiedNativeAdView
                ?: return@forUnifiedNativeAd
            populateUnifiedNativeAdView(unifiedNativeAd, adView)
            container.removeAllViews()
            container.addView(adView)
        }.withNativeAdOptions(adOptions).build().also { adLoader ->
            adLoader.loadAd(AdRequest.Builder().build())
        }
        return { release() }
    }

    private fun populateUnifiedNativeAdView(
        nativeAd: UnifiedNativeAd,
        adView: UnifiedNativeAdView
    ) {
        currentNativeAd?.destroy()
        currentNativeAd = nativeAd

        with(adView) {
            advertiserView = findViewById(R.id.ad_advertiser)
            headlineView = findViewById(R.id.ad_headline)
            bodyView = findViewById(R.id.ad_body)
            iconView = findViewById(R.id.ad_icon)
            mediaView = findViewById(R.id.ad_media)
            callToActionView = findViewById(R.id.ad_call_to_action)

            mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            advertiserView.visibility = nativeAd.advertiser
                ?.let { advertiserView.asTextView().text = it }
                ?.run { View.VISIBLE }
                ?: View.INVISIBLE
            headlineView.isVisible = nativeAd.headline
                ?.let { headlineView.asTextView().text = it }
                ?.run { true }
                ?: false
            bodyView.isVisible = nativeAd.body
                ?.let { bodyView.asTextView().text = it }
                ?.run { true }
                ?: false

            callToActionView.asTextView().text =
                nativeAd.callToAction ?: context.getString(R.string.menu_more)

            val icon = nativeAd.icon
                ?.drawable
                ?: ContextCompat.getDrawable(context, R.drawable.ic_contents_ad)
            requestManager
                .load(icon)
                .circleCrop()
                .into(iconView.asImageView())

            setNativeAd(nativeAd)
        }
    }

    fun release() {
        currentNativeAd?.destroy()
        container.removeAllViews()
    }

    companion object {

        fun from(parent: ViewGroup, requestManager: RequestManager): TasksNativeAdHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.tasks_ad_item, parent, false)
            return TasksNativeAdHolder(view, requestManager)
        }
    }
}
