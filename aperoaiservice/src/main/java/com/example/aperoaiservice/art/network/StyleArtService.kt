package com.example.aperoaiservice.art.network

import com.example.aperoaiservice.art.model.StyleArtResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface StyleArtApiService {
    @GET
    suspend fun fetchCategories(@Url url: String): StyleArtResponse
}