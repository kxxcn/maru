package dev.kxxcn.maru.util

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import dev.kxxcn.maru.R
import dev.kxxcn.maru.view.base.BaseCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class BillingManager(
    private val context: Context,
    scope: BaseCoroutineScope
) : BillingClientStateListener, PurchasesUpdatedListener, BaseCoroutineScope by scope {

    private var billingClient: BillingClient = BillingClient
        .newBuilder(context)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .enableAutoServiceReconnection()
        .setListener(this)
        .build()
        .also { it.startConnection(this) }

    private var handlePurchase: ((List<Purchase>) -> Unit)? = null

    private var handleFailure: (() -> Unit)? = null

    private var enablePurchase = false

    override fun onBillingServiceDisconnected() {
        enablePurchase = false
    }

    override fun onBillingSetupFinished(result: BillingResult) {
        enablePurchase = result.responseCode == BillingClient.BillingResponseCode.OK
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            handlePurchases(purchases.orEmpty())
        } else {
            handleFailure?.invoke()
            clearHandlers()
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        launch {
            val purchased = purchases.filter {
                it.purchaseState == Purchase.PurchaseState.PURCHASED
            }
            val acknowledged = purchased.all { acknowledgePurchase(it) }
            if (purchased.isNotEmpty() && acknowledged) {
                handlePurchase?.invoke(purchased)
            } else {
                handleFailure?.invoke()
            }
            clearHandlers()
        }
    }

    private suspend fun acknowledgePurchase(purchase: Purchase): Boolean {
        if (purchase.isAcknowledged) return true
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        return suspendCancellableCoroutine { continuation ->
            billingClient.acknowledgePurchase(params) {
                continuation.resume(it.responseCode == BillingClient.BillingResponseCode.OK)
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
            queryProductDetails(activity, onSuccess, onFailure)
        }
    }

    private fun queryProductDetails(
        activity: Activity,
        onSuccess: (List<Purchase>) -> Unit,
        onFailure: () -> Unit
    ) {
        val products = context
            .resources
            .getStringArray(R.array.purchase_items)
            .map {
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(it)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            }
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(products)
            .build()
        billingClient.queryProductDetailsAsync(params) { result, detailsResult ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                onFailure()
                return@queryProductDetailsAsync
            }
            detailsResult.productDetailsList.firstOrNull()
                ?.let {
                    launchBillingFlow(activity, it, onSuccess, onFailure)
                }
                ?: onFailure()
        }
    }

    private fun launchBillingFlow(
        activity: Activity,
        productDetails: ProductDetails,
        onSuccess: (List<Purchase>) -> Unit,
        onFailure: () -> Unit
    ) {
        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .also { builder ->
                productDetails.oneTimePurchaseOfferDetails?.offerToken
                    ?.takeIf { it.isNotBlank() }
                    ?.let { builder.setOfferToken(it) }
            }
            .build()
        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()
        handlePurchase = onSuccess
        handleFailure = onFailure
        val result = billingClient.launchBillingFlow(activity, flowParams)
        if (result.responseCode != BillingClient.BillingResponseCode.OK) {
            handleFailure?.invoke()
            clearHandlers()
        }
    }

    private fun clearHandlers() {
        handlePurchase = null
        handleFailure = null
    }

    fun release() {
        releaseCoroutine()
        billingClient.endConnection()
    }
}
