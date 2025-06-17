package com.example.aperoaiservice.art.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object VslStyleArtClient {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://dummy.apero.vn/") // dùng @Url nên có thể là bất kỳ URL hợp lệ nào
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: StyleArtApiService by lazy {
        retrofit.create(StyleArtApiService::class.java)
    }
}