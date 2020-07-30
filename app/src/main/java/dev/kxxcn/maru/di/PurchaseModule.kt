package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.purchase.PurchaseFragment
import dev.kxxcn.maru.view.purchase.PurchaseViewModel

@Module
abstract class PurchaseModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class,
            BillingModule::class
        ]
    )
    internal abstract fun purchaseFragment(): PurchaseFragment

    @Binds
    @IntoMap
    @ViewModelKey(PurchaseViewModel::class)
    abstract fun bindViewModel(viewModel: PurchaseViewModel): ViewModel
}
