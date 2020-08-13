package dev.kxxcn.maru.data.source.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    fun retrofit(url: String): Retrofit {
        val httpClient = OkHttpClient
                .Builder()
                .apply {
                    HttpLoggingInterceptor()
                            .apply { level = HttpLoggingInterceptor.Level.BODY }
                            .also { addInterceptor(it) }
                }

        return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
    }

    inline fun <reified T> create(): T {
        val url = when (T::class) {
            NaverService::class -> NaverService.URL
            else -> throw IllegalArgumentException("Unknown service class: ${T::class}")
        }
        return retrofit(url).create(T::class.java)
    }
}
