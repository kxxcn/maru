package dev.kxxcn.maru.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.gms.ads.AdSize
import dev.kxxcn.maru.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AdHelperTest {

    @Test
    fun bannerCanBeCreatedAgainAfterPreviousViewIsDestroyed() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.runOnMainSync {
            val context = instrumentation.targetContext
            val helper = AdHelper(context)
            val id = context.getString(R.string.admob_banner_home_id)
            val first = helper.createBannerAd(id, AdSize.BANNER)
            first.destroy()

            val next = helper.createBannerAd(id, AdSize.LARGE_BANNER)
            try {
                assertNotSame(first, next)
                assertEquals(AdSize.LARGE_BANNER, next.adSize)
                assertEquals(context.getString(R.string.admob_banner_test_id), next.adUnitId)
            } finally {
                next.destroy()
            }
        }
    }
}
