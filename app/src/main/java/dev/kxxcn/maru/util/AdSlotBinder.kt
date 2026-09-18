package dev.kxxcn.maru.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import dev.kxxcn.maru.R
import dev.kxxcn.maru.util.extension.px

/**
 * 네이티브 광고를 공용 광고 슬롯 레이아웃(ad_slot_view.xml)에 채운다.
 * 목록형은 미디어 없이, 더보기의 미디어형은 미디어를 보여 준다.
 */
object AdSlotBinder {

    fun bind(
        adView: NativeAdView,
        nativeAd: NativeAd,
        requestManager: RequestManager,
        showMedia: Boolean
    ) {
        val headline = adView.findViewById<TextView>(R.id.ad_headline)
        val body = adView.findViewById<TextView>(R.id.ad_body)
        val advertiser = adView.findViewById<TextView>(R.id.ad_advertiser)
        val icon = adView.findViewById<ImageView>(R.id.ad_icon)
        val media = adView.findViewById<MediaView>(R.id.ad_media)
        val callToAction = adView.findViewById<TextView>(R.id.ad_call_to_action)

        adView.headlineView = headline
        adView.bodyView = body
        adView.advertiserView = advertiser
        adView.iconView = icon
        adView.callToActionView = callToAction
        adView.mediaView = if (showMedia) media else null
        media.isVisible = showMedia

        headline.text = nativeAd.headline
        body.text = nativeAd.body
        body.isVisible = !nativeAd.body.isNullOrBlank()
        advertiser.text = nativeAd.advertiser
        advertiser.isVisible = !nativeAd.advertiser.isNullOrBlank()
        callToAction.text = nativeAd.callToAction ?: adView.context.getString(R.string.learn_more)

        val iconDrawable = nativeAd.icon?.drawable
            ?: ContextCompat.getDrawable(adView.context, R.drawable.ic_contents_ad)
        requestManager
            .load(iconDrawable)
            .transform(RoundedCorners(14.px))
            .into(icon)

        if (showMedia) {
            media.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
        }
        adView.setNativeAd(nativeAd)
    }
}
