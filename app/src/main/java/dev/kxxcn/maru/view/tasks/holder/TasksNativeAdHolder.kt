package dev.kxxcn.maru.view.tasks.holder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.AdHelper
import dev.kxxcn.maru.util.AdSlotBinder

/**
 * 체크리스트 목록형 광고 슬롯. 로드에 실패하면 슬롯을 접는다.
 */
class TasksNativeAdHolder(
    itemView: View,
    private val requestManager: RequestManager
) : RecyclerView.ViewHolder(itemView) {

    private var currentNativeAd: NativeAd? = null
    private val container: FrameLayout = itemView.findViewById(R.id.native_ad_container)
    private val adHelper = AdHelper(itemView.context)

    @SuppressLint("InflateParams")
    fun loadAd(): () -> Unit {
        val context = itemView.context
        container.isVisible = true
        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(VideoOptions.Builder().setStartMuted(true).build())
            .build()
        adHelper.createNativeAd(
            context.getString(R.string.admob_native_task_id),
            onFailed = { collapse() }
        ).forNativeAd { nativeAd ->
            val inflater = LayoutInflater.from(context)
            val adView = inflater.inflate(R.layout.ad_slot_view, null) as? NativeAdView
                ?: return@forNativeAd
            currentNativeAd?.destroy()
            currentNativeAd = nativeAd
            AdSlotBinder.bind(adView, nativeAd, requestManager, showMedia = false)
            container.removeAllViews()
            container.addView(adView)
            container.isVisible = true
        }.withNativeAdOptions(adOptions).build().also { adLoader ->
            adLoader.loadAd(AdRequest.Builder().build())
        }
        return { release() }
    }

    private fun collapse() {
        container.removeAllViews()
        container.isVisible = false
    }

    fun release() {
        currentNativeAd?.destroy()
        currentNativeAd = null
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
