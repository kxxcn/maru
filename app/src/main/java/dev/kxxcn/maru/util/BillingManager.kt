package dev.kxxcn.maru.util

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import dev.kxxcn.maru.R
import dev.kxxcn.maru.view.base.BaseCoroutineScope
import kotlinx.coroutines.launch

class BillingManager(
    private val context: Context,
    scope: BaseCoroutineScope
) : BillingClientStateListener, PurchasesUpdatedListener, BaseCoroutineScope by scope {

    private var billingClient: BillingClient = BillingClient
        .newBuilder(context)
        .enablePendingPurchases()
        .setListener(this)
        .build()
        .also { it.startConnection(this) }

    private var handlePurchase: ((List<Purchase>) -> Unit)? = null

    private var enablePurchase = false

    override fun onBillingServiceDisconnected() {
        enablePurchase = false
    }

    override fun onBillingSetupFinished(result: BillingResult) {
        enablePurchase = result.responseCode == BillingClient.BillingResponseCode.OK
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            // acknowledgePurchase(purchases)
            consumePurchase(purchases)
            handlePurchase?.invoke(purchases?.toList() ?: emptyList())
        }
        handlePurchase = null
    }

    private fun acknowledgePurchase(purchases: MutableList<Purchase>?) {
        purchases?.forEach { purchase ->
            AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
                .also { billingClient.acknowledgePurchase(it) { } }
        }
    }

    private fun consumePurchase(purchases: MutableList<Purchase>?) {
        launch {
            purchases?.forEach {
                val params = ConsumeParams
                    .newBuilder()
                    .setPurchaseToken(it.purchaseToken)
                    .build()
                billingClient.consumePurchase(params)
            }
        }
    }

    fun purchasePremium(
        activity: Activity?,
        onSuccess: (List<Purchase>) -> Unit,
        onFailure: () -> Unit
    ) {
        if (activity == null || !enablePurchase) {
            onFailure()
        } else {
            launch {
                val skuList = context
                    .resources
                    .getStringArray(R.array.purchase_items)
                    .toList()
                val params = SkuDetailsParams
                    .newBuilder()
                    .setSkusList(skuList)
                    .setType(BillingClient.SkuType.INAPP)
                billingClient.querySkuDetails(params.build())
                    .skuDetailsList
                    ?.firstOrNull()
                    ?.let {
                        handlePurchase = onSuccess
                        val flowParams = BillingFlowParams
                            .newBuilder()
                            .setSkuDetails(it)
                            .build()
                        billingClient.launchBillingFlow(activity, flowParams)
                    }
            }
        }
    }

    fun release() {
        releaseCoroutine()
        billingClient.endConnection()
    }
}
