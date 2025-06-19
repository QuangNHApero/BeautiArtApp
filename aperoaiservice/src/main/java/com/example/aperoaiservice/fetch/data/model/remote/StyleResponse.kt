package com.example.aperoaiservice.fetch.data.model.remote

internal data class StyleResponse(
    val _id: String,
    val name: String,
    val key: String,
    val config: ConfigResponse?
)

internal data class ConfigResponse(
    val positivePrompt: String?,
    val negativePrompt: String?,
    val mode: String?,
    val basemodel: String?
)