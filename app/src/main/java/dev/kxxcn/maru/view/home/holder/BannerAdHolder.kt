package dev.kxxcn.maru.view.home.holder

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.AdHelper
import dev.kxxcn.maru.util.extension.getDisplaySize

class BannerAdHolder(
    itemView: View,
    private val activity: Activity,
    private val id: String
) : RecyclerView.ViewHolder(itemView) {

    private val container: FrameLayout = itemView.findViewById(R.id.ad_view_container)
    private val adSize: AdSize?
        get() {
            val outMetrics = activity.getDisplaySize()
            val density = outMetrics.density

            var adWidthPixels = container.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
        }

    private var adView: AdView? = null

    private val adHelper = AdHelper()

    fun loadAd(): () -> Unit {
        return adHelper.createBannerAd(
            activity,
            id,
            adSize
        ).also {
            adView = it
            container.addView(it)
        }.run {
            loadAd(AdRequest.Builder().build())
            ({ release() })
        }
    }

    fun release() {
        adView?.destroy()
        container.removeAllViews()
    }

    companion object {

        fun from(parent: ViewGroup, activity: Activity): BannerAdHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.admob_item, parent, false)
            return BannerAdHolder(view, activity, context.getString(R.string.admob_banner_home_id))
        }
    }
}
