package dev.kxxcn.maru

import dev.kxxcn.maru.view.base.BaseCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MaruCoroutineScope(
    private val dispatchers: CoroutineContext = Dispatchers.IO
) : BaseCoroutineScope {

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatchers + job

    override fun releaseCoroutine() {
        job.cancel()
    }
}
