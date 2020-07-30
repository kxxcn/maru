package dev.kxxcn.maru.view.base

import kotlinx.coroutines.CoroutineScope

interface BaseCoroutineScope : CoroutineScope {

    fun releaseCoroutine()
}
