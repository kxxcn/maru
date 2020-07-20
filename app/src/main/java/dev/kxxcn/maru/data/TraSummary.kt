package dev.kxxcn.maru.data

import com.google.gson.annotations.SerializedName

data class TraSummary(
    @SerializedName("bbox")
    val bbox: List<List<Double>>,
    @SerializedName("distance")
    val distance: Int,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("fuelPrice")
    val fuelPrice: Int,
    @SerializedName("goal")
    val goal: Goal,
    @SerializedName("start")
    val start: Start,
    @SerializedName("taxiFare")
    val taxiFare: Int,
    @SerializedName("tollFare")
    val tollFare: Int,
    @SerializedName("departureTime")
    val departureTime: String
)
