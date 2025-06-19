package com.example.aperoaiservice.fetch.data.model.remote

internal data class CategoryResponse(
    val _id: String,
    val name: String,
    val styles: List<StyleResponse>
)