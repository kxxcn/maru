package dev.kxxcn.maru.data

import androidx.annotation.Keep

@Keep
data class Restore(
    val data: String = "",
    val time: Long = 0L
)
