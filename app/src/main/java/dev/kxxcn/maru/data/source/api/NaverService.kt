package dev.kxxcn.maru.data.source.api

import dev.kxxcn.maru.data.source.api.dto.DirectionDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverService {

    @Headers(
        "X-NCP-APIGW-API-KEY-ID: 9ef6ji95qg",
        "X-NCP-APIGW-API-KEY: p2VaXQD0yE37vA0lLt8rgz3MoGlR3wtTYPnVGKCa"
    )
    @GET("map-direction/v1/driving")
    suspend fun getDirections(
        @Query("start") start: String,
        @Query("goal") goal: String,
        @Query("option") option: String?
    ): DirectionDto

    companion object {

        const val URL = "https://naveropenapi.apigw.ntruss.com/"
    }
}
