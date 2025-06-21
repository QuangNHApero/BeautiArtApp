package com.example.beautisdk.ui.data.model

data class ApiResponse(
    val data: DataWrapper
)

data class DataWrapper(
    val items: List<CategoryArt>
)