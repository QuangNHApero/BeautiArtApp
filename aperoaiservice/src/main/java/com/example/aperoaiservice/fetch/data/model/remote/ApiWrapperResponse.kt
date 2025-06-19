package com.example.aperoaiservice.fetch.data.model.remote

internal data class ApiWrapperResponse<T>(
    val code: Int,
    val message: String,
    val data: T
)

internal data class CategoryDataWrapperResponse(
    val items: List<CategoryResponse>
)
