package dev.kxxcn.maru.data

import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("traoptimal")
    val traoptimal: List<TraOptimal>
)
