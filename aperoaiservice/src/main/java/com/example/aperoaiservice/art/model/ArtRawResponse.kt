package com.example.aperoaiservice.art.model

data class StyleArtResponse(
    val data: DataArtWrapper
)

data class DataArtWrapper(
    val items: List<CategoryArtRaw>
)

data class CategoryArtRaw(
    val name: String,
    val styles: List<StyleArtRaw>
)

data class StyleArtRaw(
    val _id: String,
    val name: String,
    val key: String,
    val config: ConfigArtRaw
)

data class ConfigArtRaw(
    val baseModel: String,
    val positivePrompt: String,
    val negativePrompt: String
)
