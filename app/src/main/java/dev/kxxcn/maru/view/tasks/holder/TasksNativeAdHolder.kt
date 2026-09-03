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
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.AdHelper
import dev.kxxcn.maru.util.extension.asImageView
import dev.kxxcn.maru.util.extension.asTextView

class TasksNativeAdHolder(
    itemView: View,
    private val requestManager: RequestManager
) : RecyclerView.ViewHolder(itemView) {

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

        adHelper.createNativeAd(
            context.getString(R.string.admob_native_task_id)
        ).forNativeAd { nativeAd ->
            val inflater = LayoutInflater.from(context)
            val adView = inflater.inflate(R.layout.tasks_native_view, null)
                    as? NativeAdView
                ?: return@forNativeAd
            populateNativeAdView(nativeAd, adView)
            container.removeAllViews()
            container.addView(adView)
        }.withNativeAdOptions(adOptions).build().also { adLoader ->
            adLoader.loadAd(AdRequest.Builder().build())
        }
        return { release() }
    }

    private fun populateNativeAdView(
        nativeAd: NativeAd,
        adView: NativeAdView
    ) {
        currentNativeAd?.destroy()
        currentNativeAd = nativeAd

        with(adView) {
            val advertiser = findViewById<View>(R.id.ad_advertiser)
            val headline = findViewById<View>(R.id.ad_headline)
            val body = findViewById<View>(R.id.ad_body)
            val iconView = findViewById<View>(R.id.ad_icon)
            val media = findViewById<MediaView>(R.id.ad_media)
            val callToAction = findViewById<View>(R.id.ad_call_to_action)

            advertiserView = advertiser
            headlineView = headline
            bodyView = body
            this.iconView = iconView
            mediaView = media
            callToActionView = callToAction

            media.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            advertiser.visibility = nativeAd.advertiser
                ?.let { advertiser.asTextView().text = it }
                ?.run { View.VISIBLE }
                ?: View.INVISIBLE
            headline.isVisible = nativeAd.headline
                ?.let { headline.asTextView().text = it }
                ?.run { true }
                ?: false
            body.isVisible = nativeAd.body
                ?.let { body.asTextView().text = it }
                ?.run { true }
                ?: false

            callToAction.asTextView().text =
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
