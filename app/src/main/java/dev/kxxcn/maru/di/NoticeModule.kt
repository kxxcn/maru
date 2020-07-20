package dev.kxxcn.maru.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.maru.view.notice.NoticeFragment
import dev.kxxcn.maru.view.notice.NoticeViewModel

@Module
abstract class NoticeModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun noticeFragment(): NoticeFragment

    @Binds
    @IntoMap
    @ViewModelKey(NoticeViewModel::class)
    abstract fun bindViewModel(viewModel: NoticeViewModel): ViewModel
}
