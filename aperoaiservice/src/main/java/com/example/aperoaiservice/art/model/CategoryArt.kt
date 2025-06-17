package com.example.aperoaiservice.art.model

data class CategoryArt(
    val name: String,
    val styles: List<StyleArt>
)

data class StyleArt(
    val _id: String,
    val name: String,
    val thumbnail: String,
    val baseModel: String?,
    val positivePrompt: String?,
    val negativePrompt: String?
)