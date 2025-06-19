package com.example.aperoaiservice.generate.network.request

import com.example.aperoaiservice.generate.network.model.TimeStampResponse
import retrofit2.Response
import retrofit2.http.GET

internal interface TimeStampServiceAI {
    @GET("/timestamp")
    suspend fun getTimestamp(): Response<TimeStampResponse>

}