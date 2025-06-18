package com.example.aperoaiservice.data.model.remote

internal data class CategoryResponse(
    val _id: String,
    val name: String,
    val styles: List<StyleResponse>
)