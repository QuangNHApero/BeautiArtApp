package com.example.aperoaiservice.network.model

data class AiArtParams(
    val pathImageOrigin: String,
    val styleId: String? = null,
    val positivePrompt: String? = null,
    val negativePrompt: String? = null,
    val imageSize: Number? = null,
    val mode: Int = 0,
    val baseModel: String? = null,
)