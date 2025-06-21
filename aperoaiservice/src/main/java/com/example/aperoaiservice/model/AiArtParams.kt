package com.example.aperoaiservice.model

data class AiArtParams(
    val pathImageOrigin: String,
    val styleId: String? = null,
    val style: String? = null,
    val positivePrompt: String? = null,
    val negativePrompt: String? = null
)