package com.example.aperoaiservice.fetch.data.remote

import com.example.aperoaiservice.fetch.data.model.remote.ApiWrapperResponse
import com.example.aperoaiservice.fetch.data.model.remote.CategoryDataWrapperResponse
import retrofit2.http.GET
import retrofit2.http.Url

internal interface StyleApi {
    @GET
    suspend fun getCategories(@Url url: String): ApiWrapperResponse<CategoryDataWrapperResponse>
}
