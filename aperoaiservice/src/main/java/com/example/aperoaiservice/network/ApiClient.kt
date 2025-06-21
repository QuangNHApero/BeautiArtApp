package com.example.aperoaiservice.network

import com.example.aperoaiservice.network.interceptor.SignatureInterceptor
import com.example.aperoaiservice.request.ArtServiceAI
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

internal class ApiClient {
    companion object {
        const val REQUEST_TIMEOUT: Long = 30

        @Volatile
        private var instance: ApiClient? = null

        fun getInstance(): ApiClient {
            return instance ?: synchronized(this) {
                instance ?: ApiClient().also { instance = it }
            }
        }
    }
    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(SignatureInterceptor())
            .build()
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun buildArtAi(): ArtServiceAI = buildRetrofit("https://art-premium-core.apero.vn").create()
}