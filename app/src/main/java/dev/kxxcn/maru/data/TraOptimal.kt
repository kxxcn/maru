package dev.kxxcn.maru.data

import com.google.gson.annotations.SerializedName

data class TraOptimal(
    @SerializedName("guide")
    val guide: List<Guide>,
    @SerializedName("path")
    val path: List<List<Double>>,
    @SerializedName("section")
    val section: List<Section>,
    @SerializedName("summary")
    val summary: TraSummary
)
