package dev.kxxcn.maru.view.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.billingclient.api.*
import com.orhanobut.logger.Logger
import dev.kxxcn.maru.R

class PurchaseFragment : Fragment(), PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient

    private lateinit var billingClientStateListener: BillingClientStateListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.payment_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupBillingClient()
    }

    override fun onPurchasesUpdated(result: BillingResult?, purchases: MutableList<Purchase>?) {
        if (result?.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            Logger.d(purchases)
//            for (purchase in purchases) {
//                acknowledgePurchase(purchase.purchaseToken)
//            }
        } else if (result?.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Logger.d(result.debugMessage)
        } else {
            Logger.d(result?.debugMessage)
        }
    }

    private fun setupListener() {
        billingClientStateListener = object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                Logger.d("onBillingServiceDisconnected")
            }

            override fun onBillingSetupFinished(result: BillingResult?) {
                if (result?.responseCode == BillingClient.BillingResponseCode.OK) {
                    load()
                }
            }
        }
    }

    private fun setupBillingClient() {
        val context = context ?: return
        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()
            .apply { startConnection(billingClientStateListener) }
    }

    private fun load() {
        if (billingClient.isReady) {
            val params = SkuDetailsParams.newBuilder()
                .setSkusList(listOf("premium_user"))
                .setType(BillingClient.SkuType.INAPP)
                .build()

            billingClient.querySkuDetailsAsync(params) { result, skuDetailsList ->
                if (result?.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList.isNotEmpty()) {
                    Logger.d(skuDetailsList)
//                    for (skuDetails in skuDetailsList) {
//
//                    }
                }
            }
        }
    }

    private fun acknowledgePurchase(purchaseToken: String) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()
        billingClient.acknowledgePurchase(params) { billingResult ->
            val responseCode = billingResult.responseCode
            val debugMessage = billingResult.debugMessage
            Logger.d("responseCode: $responseCode, debugMessage: $debugMessage")
        }
    }
}
