package com.example.aperoaiservice.domain.model

data class StyleArt(
    val _id : String,
    val name: String,
    val thumbnail: String,
    val positivePrompt: String?,
    val negativePrompt: String?,
    val mode: String?,
    val basemodel: String?
)