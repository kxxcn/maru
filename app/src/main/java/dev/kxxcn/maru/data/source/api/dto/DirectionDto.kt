package dev.kxxcn.maru.data.source.api.dto

import com.google.gson.annotations.SerializedName
import dev.kxxcn.maru.data.Route

data class DirectionDto(
    @SerializedName("code")
    val code: Int,
    @SerializedName("currentDateTime")
    val currentDateTime: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("route")
    val route: Route
)
