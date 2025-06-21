package com.example.aperoaiservice.network.model

import com.google.gson.annotations.SerializedName

data class AiArtRequest(
    @SerializedName("file")
    val file: String,

    @SerializedName("styleId")
    val styleId: String? = null,

    @SerializedName("positivePrompt")
    val positivePrompt: String? = null,

    @SerializedName("negativePrompt")
    val negativePrompt: String? = null,

    @SerializedName("imageSize")
    val imageSize: Number? = null,

    @SerializedName("mode")
    val mode: Int = 0,

    @SerializedName("baseModel")
    val baseModel: String? = null
)